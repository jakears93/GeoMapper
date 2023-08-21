package dev.archtech.geomapper.geomapper;

public class MapParameters {
    private String apiKey;
    private String secret;
    int zoom;
    private String mapType;
    private String latitude;
    private String longitude;

    public MapParameters(String apiKey, String secret, int zoom, String mapType) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.zoom = zoom;
        this.mapType = mapType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
