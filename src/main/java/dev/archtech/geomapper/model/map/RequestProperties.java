package dev.archtech.geomapper.model.map;

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

    public MapApiEnum getApiType() {
        return apiType;
    }

    public void setApiType(MapApiEnum apiType) {
        this.apiType = apiType;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
        this.secret = secret;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public Zoom getZoom() {
        return zoom;
    }

    public void setZoom(Zoom zoom) {
        this.zoom = zoom;
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Boolean getUseUniqueTimestamps() {
        return useUniqueTimestamps;
    }

    public void setUseUniqueTimestamps(Boolean useUniqueTimestamps) {
        this.useUniqueTimestamps = useUniqueTimestamps;
    }

    public Boolean getUseDataRange() {
        return useDataRange;
    }

    public void setUseDataRange(Boolean useDataRange) {
        this.useDataRange = useDataRange;
    }

    public Integer getDataRangeStart() {
        return dataRangeStart;
    }

    public void setDataRangeStart(Integer dataRangeStart) {
        this.dataRangeStart = dataRangeStart;
    }

    public Integer getDataRangeEnd() {
        return dataRangeEnd;
    }

    public void setDataRangeEnd(Integer dataRangeEnd) {
        this.dataRangeEnd = dataRangeEnd;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
}
