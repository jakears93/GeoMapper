package dev.archtech.geomapper.model.map;

public enum MarkerSize {
    SMALL("pin-s", "tiny"),
    LARGE("pin-l", "normal");

    private final String mapboxSize;
    private final String googleSize;

    MarkerSize(String mapboxSize, String googleSize) {
        this.mapboxSize = mapboxSize;
        this.googleSize = googleSize;
    }

    public String getMapboxSize() {
        return mapboxSize;
    }

    public String getGoogleSize() {
        return googleSize;
    }
}
