package dev.archtech.geomapper.model.map;

import lombok.Data;

@Data
public class RequestParameters {
    private String apiKey;
    private String signature;
    int zoom;
    private String mapType;
    private double latitude;
    private double longitude;
    private String markerSize;
    private String markerColour;
}
