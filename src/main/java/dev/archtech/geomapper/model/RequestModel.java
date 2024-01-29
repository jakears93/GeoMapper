package dev.archtech.geomapper.model;

import dev.archtech.geomapper.model.map.MapApiEnum;
import dev.archtech.geomapper.model.map.MapType;
import dev.archtech.geomapper.model.map.Zoom;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.Arrays;

public class RequestModel {
    private final SimpleStringProperty apiKey;
    private final SimpleStringProperty secret;
    private final SimpleListProperty<String> zoomChoices;
    private final SimpleStringProperty selectedZoom;
    private final SimpleListProperty<MapType> mapTypeChoices;
    private final SimpleObjectProperty<MapType> selectedMapType;
    private final SimpleBooleanProperty usesUniqueTimestamps;
    private final SimpleStringProperty lastDataRow;
    private final SimpleStringProperty startingRow;
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty submitStatus;
    private final SimpleBooleanProperty enableSubmitButton;
    private final SimpleBooleanProperty isRunning;
    private final SimpleBooleanProperty useRange;
    private final SimpleListProperty<MapApiEnum> mapApiType;
    private final SimpleObjectProperty<MapApiEnum> selectedApiType;

    private final static String DEFAULT_START_ROW = "2";
    private final static String DEFAULT_END_ROW = "20000";

    public RequestModel() {
        this.mapApiType = new SimpleListProperty<>(FXCollections.observableList(Arrays.stream(MapApiEnum.values()).toList()));
        this.selectedApiType = new SimpleObjectProperty<>(this.mapApiType.get(0));

        this.apiKey =  new SimpleStringProperty("");
        this.secret =  new SimpleStringProperty("");
        this.zoomChoices = new SimpleListProperty<>(FXCollections.observableList(
                Arrays.stream(Zoom.values()).map(Zoom::getLabel).toList()
        ));
        this.mapTypeChoices = new SimpleListProperty<>(FXCollections.observableList(Arrays.stream(MapType.values()).toList()));
        this.usesUniqueTimestamps = new SimpleBooleanProperty(true);
        this.lastDataRow =  new SimpleStringProperty(DEFAULT_END_ROW);
        this.startingRow =  new SimpleStringProperty(DEFAULT_START_ROW);
        this.fileName =  new SimpleStringProperty("");
        this.submitStatus =  new SimpleStringProperty("");
        this.selectedZoom = new SimpleStringProperty(this.zoomChoices.get(2));
        this.selectedMapType = new SimpleObjectProperty<>(this.mapTypeChoices.get(0));
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
    public String getSecret() {
        return secret.get();
    }
    public SimpleStringProperty secretProperty() {
        return secret;
    }
    public SimpleListProperty<String> zoomChoicesProperty() {
        return zoomChoices;
    }
    public SimpleListProperty<MapType> mapTypeChoicesProperty() {
        return mapTypeChoices;
    }
    public boolean isUseRange() {
        return useRange.get();
    }
    public SimpleBooleanProperty useRangeProperty() {
        return useRange;
    }
    public boolean isUsesUniqueTimestamps() {
        return usesUniqueTimestamps.get();
    }
    public SimpleBooleanProperty usesUniqueTimestampsProperty() {
        return usesUniqueTimestamps;
    }
    public int getLastDataRow() {
        return Integer.parseInt(this.lastDataRow.get());
    }
    public SimpleStringProperty lastDataRowProperty() {
        return lastDataRow;
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
    public SimpleStringProperty submitStatusProperty() {
        return submitStatus;
    }
    public void setSubmitStatus(String submitStatus) {
        this.submitStatus.set(submitStatus);
    }
    public SimpleStringProperty selectedZoomProperty() {
        return selectedZoom;
    }
    public SimpleObjectProperty<MapType> selectedMapTypeProperty() {
        return selectedMapType;
    }
    public SimpleBooleanProperty enableSubmitButtonProperty() {
        return enableSubmitButton;
    }
    public int getZoomValue() {
        return Zoom.fromLabel(this.selectedZoom.get()).getLevel();
    }
    public SimpleBooleanProperty isRunningProperty() {
        return isRunning;
    }
    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }
    public SimpleObjectProperty<MapApiEnum> selectedApiTypeProperty() {
        return selectedApiType;
    }
    public SimpleListProperty<MapApiEnum> mapApiTypeProperty() {
        return mapApiType;
    }
}
