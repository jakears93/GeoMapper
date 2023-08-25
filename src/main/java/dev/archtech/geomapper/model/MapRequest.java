package dev.archtech.geomapper.model;

import dev.archtech.geomapper.model.MapParameters;

import java.io.File;

public class MapRequest {
    private MapParameters mapParameters;
    private int startingRowIndex;
    private int maxDataRows;
    private boolean uniqueFlag;
    String dataFileName;
    private File dataFile;

    public MapRequest(MapParameters mapParameters, int startingRowIndex, int maxDataRows, boolean uniqueFlag, String dataFileName) {
        this.mapParameters = mapParameters;
        this.startingRowIndex = startingRowIndex;
        this.maxDataRows = maxDataRows;
        this.uniqueFlag = uniqueFlag;
        this.dataFileName = dataFileName;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public MapParameters getMapParameters() {
        return mapParameters;
    }

    public void setMapParameters(MapParameters mapParameters) {
        this.mapParameters = mapParameters;
    }

    public int getStartingRowIndex() {
        return startingRowIndex;
    }

    public void setStartingRowIndex(int startingRowIndex) {
        this.startingRowIndex = startingRowIndex;
    }

    public int getMaxDataRows() {
        return maxDataRows;
    }

    public void setMaxDataRows(int maxDataRows) {
        this.maxDataRows = maxDataRows;
    }

    public boolean isUniqueFlag() {
        return uniqueFlag;
    }

    public void setUniqueFlag(boolean uniqueFlag) {
        this.uniqueFlag = uniqueFlag;
    }

    public File getDataFile() {
        if(this.dataFile == null){
            return new File(this.dataFileName);
        }
        return this.dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public String toString() {
        return "MapRequest{" +
                "mapParameters=" + mapParameters +
                ", startingRowIndex=" + startingRowIndex +
                ", maxDataRows=" + maxDataRows +
                ", uniqueFlag=" + uniqueFlag +
                ", dataFileName='" + dataFileName + '\'' +
                '}';
    }
}
