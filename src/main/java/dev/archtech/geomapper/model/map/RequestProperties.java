package dev.archtech.geomapper.model.map;

import lombok.Data;

@Data
public class RequestProperties {
    private MapApiEnum apiType;
    private Secret secret;
    private ImageSize imageSize;
    private Zoom zoom;
    private MapType mapType;
    private Marker marker;
    private Boolean useUniqueTimestamps;
    private Boolean useDataRange;
    private Integer dataRangeStart;
    private Integer dataRangeEnd;
    private String inputFileName;
}
