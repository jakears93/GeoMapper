package dev.archtech.geomapper.service;

import dev.archtech.geomapper.model.MapParameters;
import dev.archtech.geomapper.model.MapRequest;
import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.task.ProcessTask;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ProgressBar;

public class RequestService {
    RequestModel viewModel;

    public RequestService(RequestModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.enableSubmitButtonProperty().bind(
                Bindings.createBooleanBinding(this::validateFileName, this.viewModel.fileNameProperty())
        );
    }

    public boolean validateFileName(){
        if(this.viewModel.getFileName().endsWith(".csv")){
            return true;
        }
        return false;
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
        if(this.viewModel.getMaxDataRows() < 1){
            this.viewModel.submitStatusProperty().set("Max Data Row Must Be Greater Than 0.");
            return false;
        }
        return true;
    }

    public void beginProcessRequest(ProgressBar progressBar){
        MapParameters mapParameters = new MapParameters(this.viewModel.getApiKey(), this.viewModel.getSecret(), this.viewModel.getZoomValue(), this.viewModel.getSelectedMapType().toLowerCase());
        MapRequest mapRequest = new MapRequest(mapParameters, this.viewModel.getStartingRow(), this.viewModel.getMaxDataRows(), this.viewModel.isUsesUniqueTimestamps(), this.viewModel.getFileName());

        ProcessTask task = new ProcessTask(mapRequest);
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setStyle("-fx-accent: blue;");
        task.setOnSucceeded((e)-> {
            this.viewModel.setSubmitStatus(task.getValue());
            progressBar.setStyle("-fx-accent: green;");
            this.viewModel.setDisableInput(false);

        });
        task.setOnCancelled((e) -> {
            this.viewModel.setSubmitStatus(task.getMessage());
            progressBar.setStyle("-fx-accent: yellow;");
            this.viewModel.setDisableInput(false);
        });
        task.setOnFailed((e) -> {
            this.viewModel.setSubmitStatus(task.getMessage());
            progressBar.setStyle("-fx-accent: red;");
            this.viewModel.setDisableInput(false);
        });
        new Thread(task).start();
    }
}

