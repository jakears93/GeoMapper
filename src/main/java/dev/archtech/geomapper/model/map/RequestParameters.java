package dev.archtech.geomapper.model.map;

public class RequestParameters {
    private String apiKey;
    private String signature;
    private int zoom;
    private String mapType;
    private double latitude;
    private double longitude;
    private String markerSize;
    private String markerColour;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMarkerSize() {
        return markerSize;
    }

    public void setMarkerSize(String markerSize) {
        this.markerSize = markerSize;
    }

    public String getMarkerColour() {
        return markerColour;
    }

    public void setMarkerColour(String markerColour) {
        this.markerColour = markerColour;
    }
}
