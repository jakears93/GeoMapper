package dev.archtech.geomapper.service;

import dev.archtech.geomapper.model.MapRequest;
import dev.archtech.geomapper.task.ProcessTask;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RequestService {

    private Map<String, Integer> zoomValueMap;

    public RequestService() {
        init();
    }

    private void init(){
        this.zoomValueMap = new HashMap<>();
        zoomValueMap.put("World", 1);
        zoomValueMap.put("Street", 15);
        zoomValueMap.put("Street+", 16);
        zoomValueMap.put("Street++", 17);
        zoomValueMap.put("Street+++", 18);
        zoomValueMap.put("Building", 20);
    }

    public Map<String, Integer> getZoomValueMap(){
        return this.zoomValueMap;
    }
    public int getZoomValueByKey(String key){
        return zoomValueMap.get(key);
    }

    public String selectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        if(file == null){
            return null;
        }
        return file.getAbsolutePath();
    }

    public boolean validateFileType(String value) {
        if(value.endsWith(".csv")){
            return true;
        }
//        if(value.endsWith(".xlsx")){
//            return true;
//        }
        return false;
    }

    public void beginProcessRequest(VBox window, ProgressBar progressBar, Label submitStatusLabel, MapRequest mapRequest) throws Exception {
        progressBar.setVisible(false);

        String validationError = validateParameters(mapRequest);
        if(validationError != null){
            submitStatusLabel.setText(validationError);
            return;
        }
        submitStatusLabel.setText("Processing");
        window.getChildren().stream().forEach(node -> node.setDisable(true));


        ProcessTask task = new ProcessTask(mapRequest);
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        progressBar.setStyle("-fx-accent: blue;");
        task.setOnSucceeded((e)-> {
            submitStatusLabel.setText(task.getValue());
            window.getChildren().stream().forEach(node -> node.setDisable(false));
            progressBar.setStyle("-fx-accent: green;");
        });
        task.setOnCancelled((e) -> {
            submitStatusLabel.setText(task.getMessage());
            window.getChildren().stream().forEach(node -> node.setDisable(false));
            progressBar.setStyle("-fx-accent: yellow;");
        });
        task.setOnFailed((e) -> {
            submitStatusLabel.setText(task.getMessage());
            window.getChildren().stream().forEach(node -> node.setDisable(false));
            progressBar.setStyle("-fx-accent: red;");
        });
        new Thread(task).start();
    }



    private String validateParameters(MapRequest mapRequest) {
        if(mapRequest.getMapParameters().getApiKey().length() < 15){
            return "API Key Validation Error";
        }
        if(mapRequest.getStartingRowIndex() < 1){
            return "Starting Row Must Be A Valid Data Row Number.";
        }
        return null;
    }
}
