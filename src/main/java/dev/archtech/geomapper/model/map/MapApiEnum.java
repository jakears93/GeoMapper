package dev.archtech.geomapper.model.map;

public enum MapApiEnum {
    MAPBOX("MapBox Static Images API"),
    GOOGLE("Google Static Maps API");

    private String value;

    MapApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
