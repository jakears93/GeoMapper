package dev.archtech.geomapper.service;

import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.model.map.*;
import dev.archtech.geomapper.task.ProcessTask;
import dev.archtech.geomapper.util.GPSFileReader;
import javafx.beans.binding.Bindings;

public class RequestService {
    RequestModel viewModel;

    public RequestService(RequestModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.enableSubmitButtonProperty().bind(
                Bindings.createBooleanBinding(this::validateFileName, this.viewModel.fileNameProperty())
        );
    }

    public boolean validateFileName(){
        return this.viewModel.getFileName().endsWith(".csv");
    }

    public boolean validateParameters() {
        if(this.viewModel.getApiKey().length() < 15){
            this.viewModel.submitStatusProperty().set("API Key Validation Error");
            return false;
        }
        if(this.viewModel.getStartingRow() < 2){
            this.viewModel.submitStatusProperty().set("Starting Row Must Be A Valid Data Row Number.");
            return false;
        }
        if(this.viewModel.getLastDataRow() < this.viewModel.getStartingRow()){
            this.viewModel.submitStatusProperty().set("Last Data Row Must Be Greater Than The Starting Row.");
            return false;
        }
        return true;
    }
    private int evaluateAvailableRows(String fileName, int startIndex, int endIndex) {
        int count = 0;
        try (GPSFileReader fileReader = new GPSFileReader(fileName)) {
            fileReader.skip(startIndex);
            while (fileReader.peek() != null) {
                if (count >= endIndex) {
                    break;
                }
                fileReader.readNext();
                count++;
            }
        } catch (Exception e) {
            return -1;
        }
        return count;
    }

    public ProcessTask beginProcessRequest(){
        int availableRows;
        if(!this.viewModel.isUseRange()){
            this.viewModel.setStartingRow("2");
            availableRows = evaluateAvailableRows(this.viewModel.getFileName(), this.viewModel.getStartingRow()-1, Integer.MAX_VALUE);
        }
        else {
            availableRows = evaluateAvailableRows(this.viewModel.getFileName(), this.viewModel.getStartingRow()-1, this.viewModel.getLastDataRow()-1);
        }
        if(availableRows == 0){
            this.viewModel.submitStatusProperty().set("Starting Row Larger Than Available Rows");
            return null;
        }
        else if (availableRows == -1) {
            this.viewModel.submitStatusProperty().set(String.format("Unable To Read File %s", this.viewModel.getFileName()));
            return null;
        }
        ProcessTask task = createProcessTask(availableRows);
        new Thread(task).start();
        return task;
    }

    private ProcessTask createProcessTask(int availableRows) {
        RequestProperties properties = new RequestProperties();
        Secret secret = new Secret();
        secret.setApiKey(this.viewModel.getApiKey());
        secret.setSignature(this.viewModel.getSecret());

        //TODO remove hardcoding of marker
        Marker marker = new Marker();
        marker.setColour(MarkerColour.BLUE);
        marker.setSize(MarkerSize.SMALL);

        //TODO remove hard coding of image size
        ImageSize imageSize = new ImageSize();
        imageSize.setHeight(400);
        imageSize.setWidth(400);
        properties.setImageSize(imageSize);
        properties.setApiType(this.viewModel.selectedApiTypeProperty().get());
        properties.setSecret(secret);
        properties.setZoom(Zoom.fromLevel(this.viewModel.getZoomValue()));
        //TODO remove hardcoding of mapType
        properties.setMapType(MapType.HYBRID);
        properties.setMarker(marker);
        properties.setUseUniqueTimestamps(this.viewModel.isUsesUniqueTimestamps());
        properties.setUseDataRange(this.viewModel.isUseRange());
        properties.setDataRangeStart(this.viewModel.getStartingRow()-1);
        properties.setDataRangeEnd(this.viewModel.getStartingRow()-1 + availableRows -1);
        properties.setInputFileName(this.viewModel.getFileName());

        return new ProcessTask(properties);
    }
}

