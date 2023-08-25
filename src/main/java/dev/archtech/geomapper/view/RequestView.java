package dev.archtech.geomapper.view;

import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.service.RequestService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class RequestView extends GridPane {
    private RequestModel requestModel;
    private RequestService requestService;

    private TextField apiKeyEntry;
    private TextField secretEntry;
    private ChoiceBox<String> zoomChoiceBox;
    private ChoiceBox<String> mapTypeChoiceBox;
    private CheckBox uniqueTimestampsCheckBox;
    private TextField maxDataRowsEntry;
    private TextField startingRowEntry;
    private Label fileNameLabel;
    private Button submitButton;
    private Button selectFileButton;
    private Label submitStatusLabel;
    private ProgressBar progressBar;

    private void setProperties(){
        this.apiKeyEntry = ((TextField)this.lookup("#apiKeyEntry"));
        this.secretEntry = ((TextField)this.lookup("#secretEntry"));
        this.zoomChoiceBox = ((ChoiceBox<String>)this.lookup("#zoomChoiceBox"));
        this.mapTypeChoiceBox = ((ChoiceBox<String>)this.lookup("#mapTypeChoiceBox"));
        this.uniqueTimestampsCheckBox = ((CheckBox)this.lookup("#uniqueTimestampsCheckBox"));
        this.maxDataRowsEntry = ((TextField)this.lookup("#maxDataRowsEntry"));
        this.startingRowEntry = ((TextField)this.lookup("#startingRowEntry"));
        this.fileNameLabel = ((Label)this.lookup("#fileNameLabel"));
        this.submitButton = ((Button)this.lookup("#submitButton"));
        this.selectFileButton = ((Button) this.lookup("#selectFileButton"));
        this.submitStatusLabel = ((Label)this.lookup("#submitStatusLabel"));
        this.progressBar = ((ProgressBar)this.lookup("#progressBar"));
    }

    private void bindProperties(){
        this.apiKeyEntry.textProperty().bindBidirectional(requestModel.apiKeyProperty());
        this.secretEntry.textProperty().bindBidirectional(requestModel.secretProperty());
        this.zoomChoiceBox.itemsProperty().bind(requestModel.zoomChoicesProperty());
        this.mapTypeChoiceBox.itemsProperty().bind(requestModel.mapTypeChoicesProperty());
        this.uniqueTimestampsCheckBox.selectedProperty().bindBidirectional(requestModel.usesUniqueTimestampsProperty());
        this.maxDataRowsEntry.textProperty().bindBidirectional(requestModel.maxDataRowsProperty());
        this.startingRowEntry.textProperty().bindBidirectional(requestModel.startingRowProperty());
        this.fileNameLabel.textProperty().bindBidirectional(requestModel.fileNameProperty());
        this.submitStatusLabel.textProperty().bindBidirectional(requestModel.submitStatusProperty());
        this.zoomChoiceBox.valueProperty().bindBidirectional(requestModel.selectedZoomProperty());
        this.mapTypeChoiceBox.valueProperty().bindBidirectional(requestModel.selectedMapTypeProperty());
        this.selectFileButton.setOnAction(this::onSelectFile);
        this.submitButton.setOnAction(this::onSubmitClick);
        this.submitButton.visibleProperty().bind(this.requestModel.enableSubmitButtonProperty());
        this.progressBar.visibleProperty().bind(this.requestModel.isRunningProperty());
    }

    private void bindActive(){
        this.apiKeyEntry.disableProperty().bind(this.requestModel.disableInputProperty());
        this.secretEntry.disableProperty().bind(this.requestModel.disableInputProperty());
        this.zoomChoiceBox.disableProperty().bind(this.requestModel.disableInputProperty());
        this.mapTypeChoiceBox.disableProperty().bind(this.requestModel.disableInputProperty());
        this.uniqueTimestampsCheckBox.disableProperty().bind(this.requestModel.disableInputProperty());
        this.maxDataRowsEntry.disableProperty().bind(this.requestModel.disableInputProperty());
        this.startingRowEntry.disableProperty().bind(this.requestModel.disableInputProperty());
        this.selectFileButton.disableProperty().bind(this.requestModel.disableInputProperty());
        this.submitButton.disableProperty().bind(this.requestModel.disableInputProperty());
    }

    public void init(RequestModel model){
        this.requestModel = model;
        this.requestService = new RequestService(this.requestModel);
        this.setProperties();
        this.bindProperties();
        this.bindActive();
    }

    public void onSelectFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        if(file == null){
            return;
        }
        this.requestModel.setFileName(file.getAbsolutePath());
    }

    public void onSubmitClick(ActionEvent event){
        this.requestModel.setIsRunning(false);
        if(!this.requestService.validateParameters()){
            return;
        }
        this.requestModel.setDisableInput(true);
        this.requestModel.setSubmitStatus("Processing");
        this.requestService.beginProcessRequest(this.progressBar);
        this.requestModel.setIsRunning(true);
    }
}
