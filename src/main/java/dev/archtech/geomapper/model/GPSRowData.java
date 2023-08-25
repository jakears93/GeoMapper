package dev.archtech.geomapper.model;

import java.time.LocalDateTime;

public class GPSRowData {
    private String time;
    private String longitude;
    private String latitude;

    public GPSRowData() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
