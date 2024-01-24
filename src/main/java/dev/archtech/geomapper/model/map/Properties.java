package dev.archtech.geomapper.model.map;

import java.io.File;

public class Properties {
    private MapApiEnum apiType;
    private Secret secret;
    private Zoom zoom;
    private MapType mapType;
    private Boolean useUniqueTimestamps;
    private Boolean useDataRange;
    private Integer dataRangeStart;
    private Integer dataRangeEnd;
    private String fileName;
    private File dataFile;
}
