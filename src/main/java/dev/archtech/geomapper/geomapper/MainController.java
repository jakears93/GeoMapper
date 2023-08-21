package dev.archtech.geomapper.geomapper;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainController {
    @FXML
    private VBox mainWindow;
    @FXML
    private TextField apiKeyEntry;
    @FXML
    private TextField secretEntry;
    @FXML
    private ChoiceBox<String> zoomChoiceBox;
    @FXML
    private ChoiceBox<String> mapTypeChoiceBox;
    @FXML
    private CheckBox uniqueTimestampsCheckBox;
    @FXML
    private TextField maxDataRowsEntry;
    @FXML
    private TextField startingRowEntry;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Label submitStatusLabel;
    @FXML
    private ProgressBar progressBar;

    private static Integer DEFAULT_MAX_ROWS = 20000;
    private static Integer DEFAULT_STARTING_ROW = 2;
    private final Service service;

    public MainController() {
        this.service = new Service();
    }

    @FXML
    public void initialize(){
        progressBar.setVisible(false);
        apiKeyEntry.setText("");
        submitButton.setVisible(false);
        submitButton.setOnAction(e -> {
            try {
                onSubmitClick(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        zoomChoiceBox.setItems(FXCollections.observableList(service.getZoomValueMap().keySet()
                .stream()
                .sorted((k1, k2) -> Integer.compare(service.getZoomValueByKey(k1), service.getZoomValueByKey(k2)))
                .toList()));
        zoomChoiceBox.setValue("Street");
        mapTypeChoiceBox.setItems(FXCollections.observableList(List.of(
                "Roadmap",
                "Satellite",
                "Terrain",
                "Hybrid"
        )));
        mapTypeChoiceBox.setValue("Roadmap");
        maxDataRowsEntry.setText(String.valueOf(DEFAULT_MAX_ROWS));
        startingRowEntry.setText(String.valueOf(DEFAULT_STARTING_ROW));
        uniqueTimestampsCheckBox.setSelected(true);
    }
    @FXML
    protected void onSelectFileClick(ActionEvent event){
        String value = service.selectFile(event);
        if(value == null){
            return;
        }
        else{
            fileNameLabel.setText(value);
        }
        if(service.validateFileType(value)){
            submitButton.setVisible(true);
        }
        else{
            submitButton.setVisible(false);
        }
    }

    @FXML
    protected void onSubmitClick(ActionEvent event) throws Exception {
        MapParameters parameters = new MapParameters(apiKeyEntry.getText(), secretEntry.getText(), service.getZoomValueByKey(zoomChoiceBox.getValue()), mapTypeChoiceBox.getValue().toLowerCase());
        MapRequest request = new MapRequest(parameters, Integer.valueOf(startingRowEntry.getText())-1, Integer.valueOf(maxDataRowsEntry.getText()), uniqueTimestampsCheckBox.selectedProperty().getValue(), fileNameLabel.getText());

        service.beginProcessRequest(mainWindow, progressBar, submitStatusLabel, request);
    }
}