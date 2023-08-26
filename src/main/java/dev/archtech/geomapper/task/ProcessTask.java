package dev.archtech.geomapper.task;

import com.opencsv.exceptions.CsvValidationException;
import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.GPSRowData;
import dev.archtech.geomapper.model.MapRequest;
import dev.archtech.geomapper.service.StaticMapClient;
import dev.archtech.geomapper.util.GPSFileReader;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProcessTask extends Task<String> {

    private final MapRequest mapRequest;
    private static final String OUTPUT_DIRECTORY_BASE = "output";
    private final String outputDirectory;
    private final Set<String> uniqueDateMap;

    public ProcessTask(MapRequest mapRequest) {
        this.mapRequest = mapRequest;
        this.outputDirectory = OUTPUT_DIRECTORY_BASE + File.separatorChar;
        this.uniqueDateMap =  new HashSet<>();
    }
    @Override
    protected String call() throws Exception {
        try{
            return processRequest();
        } catch (Exception e){
            this.updateMessage(e.getMessage());
            this.failed();
            throw e;
        }
    }

    private String processRequest() {
        File directory = new File(outputDirectory);
        directory.mkdir();
        int availableRows = this.mapRequest.getLastDataRowIndex() - this.mapRequest.getStartingRowIndex() +1;
        int duplicateRowCount = 0;
        try (
                StaticMapClient client = new StaticMapClient(mapRequest.getMapParameters().getApiKey(), mapRequest.getMapParameters().getSecret());
                GPSFileReader fileReader = new GPSFileReader(mapRequest.getDataFile());
        ){
            fileReader.skip(this.mapRequest.getStartingRowIndex());
            for(int i = this.mapRequest.getStartingRowIndex(); i<=this.mapRequest.getLastDataRowIndex(); i++){
                if(fileReader.peek() == null){
                    updateProgress(i+1, availableRows);
                    break;
                }
                String[] rawRowData = fileReader.readNext();
                if(this.mapRequest.isUniqueFlag()){
                    if(this.uniqueDateMap.contains(rawRowData[0])){
                        duplicateRowCount++;
                        continue;
                    }
                    this.uniqueDateMap.add(rawRowData[0]);
                }
                GPSRowData rowData = parseRowDate(rawRowData);
                mapRequest.getMapParameters().setLatitude(Double.parseDouble(rowData.getLatitude()));
                mapRequest.getMapParameters().setLongitude(Double.parseDouble(rowData.getLongitude()));

                String fileName = generateFileName(i, rowData);
                byte[] imageDate = client.submitRequest(mapRequest.getMapParameters());
                saveImage(imageDate, fileName);
                updateProgress(i+1, availableRows);
            }
            updateProgress(100,100);
            return String.format("Successfully Processed %s Data Rows", availableRows-duplicateRowCount);
        } catch (FileNotFoundException e) {
            updateProgress(100,100);
            e.printStackTrace();
            throw new FailedRequestException(String.format("Unable To Open File %s", mapRequest.getDataFileName()));
        } catch (IOException | CsvValidationException e) {
            updateProgress(100,100);
            e.printStackTrace();
            throw new FailedRequestException(String.format("Unable To Read File %s", mapRequest.getDataFileName()));
        } catch (FailedRequestException e) {
            throw e;
        } catch (Exception e) {
            updateProgress(100,100);
            e.printStackTrace();
            throw new FailedRequestException(e.getLocalizedMessage());
        }
    }

    private GPSRowData parseRowDate(String[] rawRowData) {
        GPSRowData rowData = new GPSRowData();
        rowData.setTime(rawRowData[0]);
        rowData.setLatitude(rawRowData[2]);
        rowData.setLongitude(rawRowData[3]);
        return rowData;
    }

    private String generateFileName(int rowIndex, GPSRowData rowData) {
        return outputDirectory +
                (rowIndex + 1) +
                "__" +
                rowData.getTime().replace(" ", "-").replace("/", "-").replace(":", ",") +
                "_" +
                rowData.getLatitude().replace(".", ",") +
                "_" +
                rowData.getLongitude().replace(".", ",") +
                ".png";
    }

    private void saveImage(byte[] data, String fileName){
        File targetFile = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(data);
        } catch (IOException e) {
            updateProgress(100L,100L);
            e.printStackTrace();
            throw new FailedRequestException(String.format("Failed To Save File %s", fileName));
        }
    }
}