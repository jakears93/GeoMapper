package dev.archtech.geomapper.model;

public class MapParameters {
    private String apiKey;
    private String secret;
    int zoom;
    private String mapType;
    private double latitude;
    private double longitude;

    public MapParameters(String apiKey, String secret, int zoom, String mapType) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.zoom = zoom;
        this.mapType = mapType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public int getZoom() {
        return zoom;
    }

    public String getMapType() {
        return mapType;
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
}
