package dev.archtech.geomapper.geomapper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.concurrent.Task;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class ProcessTask extends Task<String> {

    private final MapRequest mapRequest;
    private static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private static final int HTTP_SUCCESS = 200;
    private static final String OUTPUT_DIRECTORY_BASE = "output";
    private String outputDirectory;
    private Set<String> uniqueDateMap;

    public ProcessTask(MapRequest mapRequest) {
        this.mapRequest = mapRequest;
        this.outputDirectory = OUTPUT_DIRECTORY_BASE + File.separatorChar;
        this.uniqueDateMap =  new HashSet<>();
    }
    @Override
    protected String call() throws Exception {
        File directory = new File(outputDirectory);
        directory.mkdir();
        int numOfRows = evaluateFile(this.mapRequest.getDataFile(), this.mapRequest.getStartingRowIndex(), this.mapRequest.getMaxDataRows());
        if(numOfRows == 0){
            this.updateMessage("Starting Row Number Too Large");
            this.cancel();
        }

        FileReader reader = new FileReader(this.mapRequest.getDataFile());
        CSVReader csvReader = new CSVReader(reader);
        csvReader.skip(this.mapRequest.getStartingRowIndex());

        int duplicateRowCount = 0;
        for(int i=this.mapRequest.getStartingRowIndex(); i<numOfRows+this.mapRequest.getStartingRowIndex() || i-this.mapRequest.getStartingRowIndex() > this.mapRequest.getMaxDataRows(); i++){
            if(csvReader.peek() == null){
                System.out.println(i);
                System.out.println(numOfRows);
                updateProgress(i, numOfRows-1);
                break;
            }
            String[] rawRowData = csvReader.readNext();
            if(this.mapRequest.isUniqueFlag()){
                if(this.uniqueDateMap.contains(rawRowData[0])){
                    duplicateRowCount++;
                    continue;
                }
                this.uniqueDateMap.add(rawRowData[0]);
            }
            GPSRowData rowData = parseRowDate(rawRowData);
            mapRequest.getMapParameters().setLatitude(rowData.getLatitude());
            mapRequest.getMapParameters().setLongitude(rowData.getLongitude());
            HttpGet request;
            if(mapRequest.getMapParameters().getSecret().equals("")){
                request = buildRequest(mapRequest.getMapParameters());
            }
            else{
                request = buildSignedRequest(mapRequest.getMapParameters());
            }
            try (CloseableHttpClient client = HttpClients.createDefault()){
                CloseableHttpResponse response = client.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                if(statusCode != HTTP_SUCCESS){
                    throw new FailedRequestException(statusCode, i+1);
                }
                String fileName = generateFileName(i, rowData);
                saveImage(response.getEntity().getContent(), fileName);
            } catch (FailedRequestException e){
                this.updateMessage(e.getMessage());
                this.cancel();
                csvReader.close();
                reader.close();
            }
            updateProgress(i, numOfRows-1);
        }
        csvReader.close();
        reader.close();
        updateProgress(100,100);
        return String.format("Successfully Processed %s Data Rows", numOfRows-duplicateRowCount);
    }

    private GPSRowData parseRowDate(String[] rawRowData) {
        GPSRowData rowData = new GPSRowData();
        rowData.setTime(rawRowData[0]);
        rowData.setLatitude(rawRowData[2]);
        rowData.setLongitude(rawRowData[3]);
        return rowData;
    }

    private String generateFileName(int rowIndex, GPSRowData rowData) {
        StringBuilder pathBuilder = new StringBuilder(outputDirectory)
                .append(rowIndex+1)
                .append("_")
                .append(rowData.getTime().replace(" ", "-").replace("/", "-"))
                .append("_")
                .append(rowData.getLatitude().replace(".", ","))
                .append("_")
                .append(rowData.getLongitude().replace(".",","))
                .append(".png");
        return pathBuilder.toString();
    }

    private int evaluateFile(File file, int start, int max) throws IOException, CsvValidationException {
        FileReader reader = new FileReader(file);
        CSVReader csvReader = new CSVReader(reader);
        csvReader.skip(start);
        int count = 0;
        while(csvReader.peek()!=null){
            if(count >= max){
                break;
            }
            csvReader.readNext();
            count++;
        }
        csvReader.close();
        return count;
    }

    private HttpGet buildRequest(MapParameters mapParameters) {
        try{
            URI uri = new URIBuilder(API_BASE_URL)
                    .addParameter("center", mapParameters.getLatitude()+","+mapParameters.getLongitude())
                    .addParameter("zoom", String.valueOf(mapParameters.getZoom()))
                    .addParameter("markers", "size:tiny%7Ccolor:blue%7C"+mapParameters.getLatitude()+","+mapParameters.getLongitude())
                    .addParameter("maptype", mapParameters.getMapType())
                    .addParameter("size", "400x400")
                    .addParameter("key", mapParameters.getApiKey())
                    .build();
            return new HttpGet(uri);
        } catch (URISyntaxException e){
            throw new FailedRequestException("Failed to Build request");
        }
    }

    private HttpGet buildSignedRequest(MapParameters mapParameters) {
        this.updateMessage("Unable to Sign Request");
        updateProgress(100,100);
        this.cancel();
        return null;
    }

    private void saveImage(InputStream data, String fileName){
        File targetFile = new File(fileName);
        try {
            java.nio.file.Files.copy(
                    data,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            data.close();
        } catch (IOException e) {
            throw new FailedRequestException(String.format("Failed To Save File %s", fileName));
        }
    }
}
