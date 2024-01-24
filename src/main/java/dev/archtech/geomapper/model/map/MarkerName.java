package dev.archtech.geomapper.model.map;

public enum MarkerName {
    SMALL("pin-s", "tiny"),
    LARGE("pin-l", "normal");

    private String mapboxSize;
    private String googleSize;

    MarkerName(String mapboxSize, String googleSize) {
        this.mapboxSize = mapboxSize;
        this.googleSize = googleSize;
    }
}
