package dev.archtech.geomapper.task;

import com.opencsv.exceptions.CsvValidationException;
import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.GPSRowData;
import dev.archtech.geomapper.model.client.StaticMapClient;
import dev.archtech.geomapper.model.map.Coordinates;
import dev.archtech.geomapper.model.map.RequestParameters;
import dev.archtech.geomapper.model.map.RequestProperties;
import dev.archtech.geomapper.util.GPSFileReader;
import dev.archtech.geomapper.util.ParameterResolver;
import dev.archtech.geomapper.util.StaticMapClientFactory;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProcessTask extends Task<String> {

    private final RequestProperties properties;
    private static final String OUTPUT_DIRECTORY_BASE = "output";
    private final String outputDirectory;
    private final Set<String> uniqueDateSet;

    public ProcessTask(RequestProperties properties) {
        this.properties = properties;
        this.outputDirectory = OUTPUT_DIRECTORY_BASE + File.separatorChar;
        this.uniqueDateSet =  new HashSet<>();
    }
    @Override
    protected String call() {
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
        int availableRows = this.properties.getDataRangeEnd() - this.properties.getDataRangeStart() +1;
        int duplicateRowCount = 0;
        try (
                StaticMapClient client = StaticMapClientFactory.createClient(this.properties);
                GPSFileReader fileReader = new GPSFileReader(this.properties.getInputFileName());
        ){
            fileReader.skip(this.properties.getDataRangeStart());
            for(int i = this.properties.getDataRangeStart(); i<=this.properties.getDataRangeEnd(); i++){
                if(fileReader.peek() == null){
                    updateProgress(i+1, availableRows);
                    break;
                }
                String[] rawRowData = fileReader.readNext();
                if(this.properties.getUseUniqueTimestamps()){
                    if(this.uniqueDateSet.contains(rawRowData[0])){
                        duplicateRowCount++;
                        continue;
                    }
                    this.uniqueDateSet.add(rawRowData[0]);
                }
                GPSRowData rowData = parseRowDate(i, rawRowData);
                RequestParameters parameters = ParameterResolver.resolveParameters(this.properties, rowData);

                String fileName = generateFileName(rowData);
                byte[] imageBytes = client.submitRequest(parameters);
                saveImage(imageBytes, fileName);
                updateProgress(i+1, availableRows);
            }
            updateProgress(100,100);
            return String.format("Successfully Processed %s Data Rows", availableRows-duplicateRowCount);
        } catch (FileNotFoundException e) {
            updateProgress(100,100);
            System.err.println(e.getMessage());
            throw new FailedRequestException(String.format("Unable To Open File %s", this.properties.getInputFileName()));
        } catch (IOException | CsvValidationException e) {
            updateProgress(100,100);
            System.err.println(e.getMessage());
            throw new FailedRequestException(String.format("Unable To Read File %s", this.properties.getInputFileName()));
        } catch (FailedRequestException e) {
            throw e;
        } catch (Exception e) {
            updateProgress(100,100);
            System.err.println(e.getMessage());
            throw new FailedRequestException(e.getLocalizedMessage());
        }
    }

    private GPSRowData parseRowDate(int index, String[] rawRowData) {
        GPSRowData rowData = new GPSRowData();
        rowData.setIndex(index);
        rowData.setTime(rawRowData[0]);
        rowData.setCoordinates(new Coordinates());
        rowData.getCoordinates().setLatitude(Double.parseDouble(rawRowData[2]));
        rowData.getCoordinates().setLongitude(Double.parseDouble(rawRowData[3]));
        return rowData;
    }

    private String generateFileName(GPSRowData rowData) {
        return outputDirectory +
                (rowData.getIndex() + 1) +
                "__" +
                rowData.getTime().replace(" ", "-").replace("/", "-").replace(":", ",") +
                "_" +
                rowData.getCoordinates().getLatitude().toString().replace(".", ",") +
                "_" +
                rowData.getCoordinates().getLongitude().toString().replace(".", ",") +
                ".png";
    }

    private void saveImage(byte[] data, String fileName){
        File targetFile = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(data);
        } catch (IOException e) {
            updateProgress(100L,100L);
            System.err.println(e.getMessage());
            throw new FailedRequestException(String.format("Failed To Save File %s", fileName));
        }
    }
}