package dev.archtech.geomapper.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Map;

public class RequestModel {
    private final SimpleStringProperty apiKey;
    private final SimpleStringProperty secret;
    private final SimpleListProperty<String> zoomChoices;
    private final SimpleStringProperty selectedZoom;
    private final SimpleListProperty<String> mapTypeChoices;
    private final SimpleStringProperty selectedMapType;
    private final SimpleBooleanProperty usesUniqueTimestamps;
    private final SimpleStringProperty lastDataRow;
    private final SimpleStringProperty startingRow;
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty submitStatus;
    private final SimpleBooleanProperty enableSubmitButton;
    private final SimpleBooleanProperty isRunning;
    private final SimpleBooleanProperty useRange;
    private final Map<String, Integer> zoomValueMap = Map.of(
    "World", 1,
    "Street", 15,
    "Street+", 16,
    "Street++", 17,
    "Street+++", 18,
    "Building", 20
    );

    public RequestModel() {
        this.apiKey =  new SimpleStringProperty("");
        this.secret =  new SimpleStringProperty("");
        this.zoomChoices = new SimpleListProperty<>(FXCollections.observableList(this.zoomValueMap.keySet()
                .stream()
                .sorted((k1, k2) -> Integer.compare(this.zoomValueMap.get(k1), this.zoomValueMap.get(k2)))
                .toList()));
        this.mapTypeChoices = new SimpleListProperty<>(FXCollections.observableList(List.of(
                "Roadmap",
                "Satellite",
                "Terrain",
                "Hybrid"
        )));
        this.usesUniqueTimestamps = new SimpleBooleanProperty(true);
        this.lastDataRow =  new SimpleStringProperty("20000");
        this.startingRow =  new SimpleStringProperty("2");
        this.fileName =  new SimpleStringProperty("");
        this.submitStatus =  new SimpleStringProperty("");
        this.selectedZoom = new SimpleStringProperty(this.zoomChoices.get(2));
        this.selectedMapType = new SimpleStringProperty(this.mapTypeChoices.get(0));
        this.useRange = new SimpleBooleanProperty(false);
        this.enableSubmitButton = new SimpleBooleanProperty();
        this.isRunning = new SimpleBooleanProperty(false);
    }

    public String getApiKey() {
        return apiKey.get();
    }

    public SimpleStringProperty apiKeyProperty() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey.set(apiKey);
    }

    public String getSecret() {
        return secret.get();
    }

    public SimpleStringProperty secretProperty() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret.set(secret);
    }

    public ObservableList<String> getZoomChoices() {
        return zoomChoices.get();
    }

    public SimpleListProperty<String> zoomChoicesProperty() {
        return zoomChoices;
    }

    public void setZoomChoices(ObservableList<String> zoomChoices) {
        this.zoomChoices.set(zoomChoices);
    }

    public ObservableList<String> getMapTypeChoices() {
        return mapTypeChoices.get();
    }

    public SimpleListProperty<String> mapTypeChoicesProperty() {
        return mapTypeChoices;
    }

    public void setMapTypeChoices(ObservableList<String> mapTypeChoices) {
        this.mapTypeChoices.set(mapTypeChoices);
    }

    public boolean isUseRange() {
        return useRange.get();
    }

    public SimpleBooleanProperty useRangeProperty() {
        return useRange;
    }

    public void setUseRange(boolean useRange) {
        this.useRange.set(useRange);
    }

    public boolean isUsesUniqueTimestamps() {
        return usesUniqueTimestamps.get();
    }

    public SimpleBooleanProperty usesUniqueTimestampsProperty() {
        return usesUniqueTimestamps;
    }

    public void setUsesUniqueTimestamps(boolean usesUniqueTimestamps) {
        this.usesUniqueTimestamps.set(usesUniqueTimestamps);
    }

    public int getLastDataRow() {
        return Integer.parseInt(this.lastDataRow.get());
    }

    public SimpleStringProperty lastDataRowProperty() {
        return lastDataRow;
    }

    public void setLastDataRow(String lastDataRow) {
        this.lastDataRow.set(lastDataRow);
    }

    public int getStartingRow() {
        return Integer.parseInt(this.startingRow.get());
    }

    public SimpleStringProperty startingRowProperty() {
        return startingRow;
    }

    public void setStartingRow(String startingRow) {
        this.startingRow.set(startingRow);
    }

    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getSubmitStatus() {
        return submitStatus.get();
    }

    public SimpleStringProperty submitStatusProperty() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus.set(submitStatus);
    }

    public String getSelectedZoom() {
        return selectedZoom.get();
    }

    public SimpleStringProperty selectedZoomProperty() {
        return selectedZoom;
    }

    public void setSelectedZoom(String selectedZoom) {
        this.selectedZoom.set(selectedZoom);
    }

    public String getSelectedMapType() {
        return selectedMapType.get();
    }

    public SimpleStringProperty selectedMapTypeProperty() {
        return selectedMapType;
    }

    public void setSelectedMapType(String selectedMapType) {
        this.selectedMapType.set(selectedMapType);
    }

    public boolean isEnableSubmitButton() {
        return enableSubmitButton.get();
    }

    public SimpleBooleanProperty enableSubmitButtonProperty() {
        return enableSubmitButton;
    }

    public void setEnableSubmitButton(boolean enableSubmitButton) {
        this.enableSubmitButton.set(enableSubmitButton);
    }

    public int getZoomValue() {
        return zoomValueMap.get(this.selectedZoom.get());
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public SimpleBooleanProperty isRunningProperty() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }
}
