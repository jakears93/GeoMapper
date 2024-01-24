package dev.archtech.geomapper.model.map;

public enum MapApiEnum {
    GOOGLE("Google Static Maps API"),
    MAPBOX("MapBox Static Images API");

    private String value;

    MapApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
