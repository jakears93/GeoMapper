package dev.archtech.geomapper.model;

import java.io.File;

public class MapRequest {
    private MapParameters mapParameters;
    private int startingRowIndex;
    private int lastDataRowIndex;
    private boolean uniqueFlag;
    String dataFileName;
    private File dataFile;

    public MapRequest(MapParameters mapParameters, int startingRowIndex, int lastDataRowIndex, boolean uniqueFlag, String dataFileName) {
        this.mapParameters = mapParameters;
        this.startingRowIndex = startingRowIndex;
        this.lastDataRowIndex = lastDataRowIndex;
        this.uniqueFlag = uniqueFlag;
        this.dataFileName = dataFileName;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public MapParameters getMapParameters() {
        return mapParameters;
    }

    public int getStartingRowIndex() {
        return startingRowIndex;
    }

    public int getLastDataRowIndex() {
        return lastDataRowIndex;
    }

    public boolean isUniqueFlag() {
        return uniqueFlag;
    }

    public File getDataFile() {
        if(this.dataFile == null){
            return new File(this.dataFileName);
        }
        return this.dataFile;
    }

    public void setLastDataRowIndex(int lastDataRowIndex) {
        this.lastDataRowIndex = lastDataRowIndex;
    }

    @Override
    public String toString() {
        return "MapRequest{" +
                "mapParameters=" + mapParameters +
                ", startingRowIndex=" + startingRowIndex +
                ", lastDataRowIndex=" + lastDataRowIndex +
                ", uniqueFlag=" + uniqueFlag +
                ", dataFileName='" + dataFileName + '\'' +
                '}';
    }
}
