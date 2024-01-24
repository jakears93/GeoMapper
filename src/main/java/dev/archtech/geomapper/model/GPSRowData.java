package dev.archtech.geomapper.model;

import dev.archtech.geomapper.model.map.Coordinates;
public class GPSRowData {
    private int index;
    private String time;
    private Coordinates coordinates;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
