package dev.archtech.geomapper.service;

import com.opencsv.exceptions.CsvValidationException;
import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.MapParameters;
import dev.archtech.geomapper.model.MapRequest;
import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.task.ProcessTask;
import dev.archtech.geomapper.util.GPSFileReader;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        try (GPSFileReader fileReader = new GPSFileReader(new File(fileName))) {
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
        MapParameters mapParameters = new MapParameters(this.viewModel.getApiKey(), this.viewModel.getSecret(), this.viewModel.getZoomValue(), this.viewModel.getSelectedMapType().toLowerCase());
        MapRequest mapRequest = new MapRequest(mapParameters, this.viewModel.getStartingRow()-1, this.viewModel.getLastDataRow()-1, this.viewModel.isUsesUniqueTimestamps(), this.viewModel.getFileName());
        int availableRows = evaluateAvailableRows(mapRequest.getDataFileName(), mapRequest.getStartingRowIndex(), mapRequest.getLastDataRowIndex());
        if(availableRows == 0){
            this.viewModel.submitStatusProperty().set("Starting Row Larger Than Available Rows");
            return null;
        }
        else if (availableRows == -1) {
            this.viewModel.submitStatusProperty().set(String.format("Unable To Read File %s", mapRequest.getDataFileName()));
            return null;
        }
        mapRequest.setLastDataRowIndex(mapRequest.getStartingRowIndex() + availableRows -1);

        ProcessTask task = new ProcessTask(mapRequest);
        new Thread(task).start();
        return task;
    }
}

