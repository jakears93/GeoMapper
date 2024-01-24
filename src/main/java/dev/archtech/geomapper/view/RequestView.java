package dev.archtech.geomapper.view;

import dev.archtech.geomapper.model.RequestModel;
import dev.archtech.geomapper.service.RequestService;
import dev.archtech.geomapper.task.ProcessTask;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class RequestView extends GridPane {
    private ChoiceBox<String> mapApiType;
    private RequestModel requestModel;
    private RequestService requestService;
    private PasswordField apiKeyEntry;
    private PasswordField secretEntry;
    private ChoiceBox<String> zoomChoiceBox;
    private ChoiceBox<String> mapTypeChoiceBox;
    private CheckBox useRangeCheckBox;
    private CheckBox uniqueTimestampsCheckBox;
    private TextField lastDataRowEntry;
    private TextField startingRowEntry;
    private Label fileNameLabel;
    private Button submitButton;
    private Button selectFileButton;
    private Label submitStatusLabel;
    private ProgressBar progressBar;
    private Label startingRowLabel;
    private Label lastDataRowLabel;

    private void setProperties(){
        this.mapApiType = ((ChoiceBox<String>)this.lookup("#mapApiType"));
        this.apiKeyEntry = ((PasswordField)this.lookup("#apiKeyEntry"));
        this.secretEntry = ((PasswordField)this.lookup("#secretEntry"));
        this.zoomChoiceBox = ((ChoiceBox<String>)this.lookup("#zoomChoiceBox"));
        this.mapTypeChoiceBox = ((ChoiceBox<String>)this.lookup("#mapTypeChoiceBox"));
        this.useRangeCheckBox = ((CheckBox)this.lookup("#useRangeCheckBox"));
        this.uniqueTimestampsCheckBox = ((CheckBox)this.lookup("#uniqueTimestampsCheckBox"));
        this.lastDataRowEntry = ((TextField)this.lookup("#lastDataRowEntry"));
        this.startingRowEntry = ((TextField)this.lookup("#startingRowEntry"));
        this.fileNameLabel = ((Label)this.lookup("#fileNameLabel"));
        this.submitButton = ((Button)this.lookup("#submitButton"));
        this.selectFileButton = ((Button) this.lookup("#selectFileButton"));
        this.submitStatusLabel = ((Label)this.lookup("#submitStatusLabel"));
        this.progressBar = ((ProgressBar)this.lookup("#progressBar"));
        this.startingRowLabel = ((Label)this.lookup("#startingRowLabel"));
        this.lastDataRowLabel = ((Label)this.lookup("#lastDataRowLabel"));
    }

    private void bindValues(){
        this.mapApiType.itemsProperty().bind(requestModel.mapApiTypeProperty());
        this.mapApiType.valueProperty().bindBidirectional(requestModel.selectedApiTypeProperty());
        this.apiKeyEntry.textProperty().bindBidirectional(requestModel.apiKeyProperty());
        this.secretEntry.textProperty().bindBidirectional(requestModel.secretProperty());
        this.zoomChoiceBox.itemsProperty().bind(requestModel.zoomChoicesProperty());
        this.mapTypeChoiceBox.itemsProperty().bind(requestModel.mapTypeChoicesProperty());
        this.useRangeCheckBox.selectedProperty().bindBidirectional(requestModel.useRangeProperty());
        this.uniqueTimestampsCheckBox.selectedProperty().bindBidirectional(requestModel.usesUniqueTimestampsProperty());
        this.lastDataRowEntry.textProperty().bindBidirectional(requestModel.lastDataRowProperty());
        this.startingRowEntry.textProperty().bindBidirectional(requestModel.startingRowProperty());
        this.fileNameLabel.textProperty().bindBidirectional(requestModel.fileNameProperty());
        this.submitStatusLabel.textProperty().bindBidirectional(requestModel.submitStatusProperty());
        this.zoomChoiceBox.valueProperty().bindBidirectional(requestModel.selectedZoomProperty());
        this.mapTypeChoiceBox.valueProperty().bindBidirectional(requestModel.selectedMapTypeProperty());
        this.selectFileButton.setOnAction(this::onSelectFile);
        this.submitButton.setOnAction(this::onSubmitClick);
    }

    private void bindActive(){
        this.apiKeyEntry.disableProperty().bind(this.requestModel.isRunningProperty());
        this.secretEntry.disableProperty().bind(this.requestModel.isRunningProperty());
        this.zoomChoiceBox.disableProperty().bind(this.requestModel.isRunningProperty());
        this.mapTypeChoiceBox.disableProperty().bind(this.requestModel.isRunningProperty());
        this.uniqueTimestampsCheckBox.disableProperty().bind(this.requestModel.isRunningProperty());
        this.lastDataRowEntry.disableProperty().bind(this.requestModel.isRunningProperty());
        this.startingRowEntry.disableProperty().bind(this.requestModel.isRunningProperty());
        this.selectFileButton.disableProperty().bind(this.requestModel.isRunningProperty());
        this.submitButton.disableProperty().bind(this.requestModel.isRunningProperty());
        this.useRangeCheckBox.disableProperty().bind(this.requestModel.isRunningProperty());
    }

    private void bindVisibility(){
        this.progressBar.visibleProperty().bind(this.requestModel.isRunningProperty());
        this.submitButton.visibleProperty().bind(this.requestModel.enableSubmitButtonProperty());
        this.startingRowLabel.visibleProperty().bind(this.requestModel.useRangeProperty());
        this.lastDataRowLabel.visibleProperty().bind(this.requestModel.useRangeProperty());
        this.startingRowEntry.visibleProperty().bind(this.requestModel.useRangeProperty());
        this.lastDataRowEntry.visibleProperty().bind(this.requestModel.useRangeProperty());
    }

    public void init(RequestModel model){
        this.requestModel = model;
        this.requestService = new RequestService(this.requestModel);
        this.setProperties();
        this.bindValues();
        this.bindActive();
        this.bindVisibility();
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
        if(!this.requestService.validateParameters()){
            return;
        }
        this.requestModel.setSubmitStatus("Processing");

        ProcessTask task = this.requestService.beginProcessRequest();
        if(task == null){
            return;
        }

        this.requestModel.setIsRunning(true);
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setStyle("-fx-accent: blue;");

        task.setOnSucceeded((e)-> {
            this.requestModel.setSubmitStatus(task.getValue());
            progressBar.setStyle("-fx-accent: green;");
            this.requestModel.setIsRunning(false);

        });
        task.setOnCancelled((e) -> {
            this.requestModel.setSubmitStatus(task.getMessage());
            progressBar.setStyle("-fx-accent: yellow;");
            this.requestModel.setIsRunning(false);
        });
        task.setOnFailed((e) -> {
            this.requestModel.setSubmitStatus(task.getMessage());
            progressBar.setStyle("-fx-accent: red;");
            this.requestModel.setIsRunning(false);
        });
    }
}
