package dev.archtech.geomapper.model.map;

public enum MapType {
    ROADMAP("outdoors-v12", "Roadmap"),
    SATELLITE("satellite-v9","Satellite"),
    TERRAIN("outdoors-v12","Terrain"),
    HYBRID("satellite-streets-v12","Hybrid");

    private String mapboxString;
    private String googleString;

    MapType(String mapboxString, String googleString) {
        this.mapboxString = mapboxString;
        this.googleString = googleString;
    }

    public String getMapboxString() {
        return mapboxString;
    }

    public String getGoogleString() {
        return googleString;
    }
}
