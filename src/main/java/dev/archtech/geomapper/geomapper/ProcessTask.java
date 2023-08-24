package dev.archtech.geomapper.geomapper;

import com.opencsv.exceptions.CsvValidationException;
import javafx.concurrent.Task;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ProcessTask extends Task<String> {

    private final MapRequest mapRequest;
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
        int numOfRows = evaluateAvailableRows(this.mapRequest.getDataFile(), this.mapRequest.getStartingRowIndex(), this.mapRequest.getMaxDataRows());
        if(numOfRows == 0){
            throw new FailedRequestException("Starting Row Larger Than Available Rows");
        }

        int duplicateRowCount = 0;
        try (
                StaticMapClient client = new StaticMapClient(mapRequest.getMapParameters().getApiKey(), mapRequest.getMapParameters().getSecret());
                GPSFileReader fileReader = new GPSFileReader(mapRequest.getDataFile());
        ){
            fileReader.skip(this.mapRequest.getStartingRowIndex());
            for(int i=this.mapRequest.getStartingRowIndex(); i<numOfRows+this.mapRequest.getStartingRowIndex() || i-this.mapRequest.getStartingRowIndex() > this.mapRequest.getMaxDataRows(); i++){
                if(fileReader.peek() == null){
                    System.out.println(i);
                    System.out.println(numOfRows);
                    updateProgress(i, numOfRows-1);
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
                updateProgress(i, numOfRows-1);
            }
            updateProgress(100,100);
            return String.format("Successfully Processed %s Data Rows", numOfRows-duplicateRowCount);
        } catch (FileNotFoundException e) {
            throw new FailedRequestException(String.format("Unable To Open File %s", mapRequest.getDataFileName()));
        } catch (IOException | CsvValidationException e) {
            throw new FailedRequestException(String.format("Unable To Read File %s", mapRequest.getDataFileName()));
        } catch (FailedRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedRequestException(String.format("Error With File %s", mapRequest.getDataFileName()));
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
        StringBuilder pathBuilder = new StringBuilder(outputDirectory)
                .append(rowIndex+1)
                .append("__")
                .append(rowData.getTime().replace(" ", "-").replace("/", "-").replace(":", ","))
                .append("_")
                .append(rowData.getLatitude().replace(".", ","))
                .append("_")
                .append(rowData.getLongitude().replace(".",","))
                .append(".png");
        return pathBuilder.toString();
    }

    private int evaluateAvailableRows(File file, int start, int max) {
        try (GPSFileReader fileReader = new GPSFileReader(file)) {
            fileReader.skip(start);
            int count = 0;
            while (fileReader.peek() != null) {
                if (count >= max) {
                    break;
                }
                fileReader.readNext();
                count++;
            }
            if(count == 0){
                throw new FailedRequestException("Starting Row Larger Than Available Rows");
            }
            return count;
        } catch (FileNotFoundException e) {
            throw new FailedRequestException(String.format("Unable To Open File %s", mapRequest.getDataFileName()));
        } catch (IOException | CsvValidationException e) {
            throw new FailedRequestException(String.format("Unable To Read File %s", mapRequest.getDataFileName()));
        } catch (Exception e) {
            throw new FailedRequestException(String.format("Error With File %s", mapRequest.getDataFileName()));
        }
    }

    private void saveImage(byte[] data, String fileName){
        File targetFile = new File(fileName);
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new FailedRequestException(String.format("Failed To Save File %s", fileName));
        }
    }
}