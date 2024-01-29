package dev.archtech.geomapper.model.map;

public enum MapApiEnum {
    MAPBOX_STATIC_IMAGE("MapBox Static Images API"),
    GOOGLE_STATIC_IMAGE("Google Static Maps API");

    private final String value;

    MapApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
