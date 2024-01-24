package dev.archtech.geomapper.model.map;

public enum MapType {
    ROADMAP("RoadMap", "outdoors-v12", "roadmap"),
    SATELLITE("Satellite", "satellite-v9","satellite"),
    TERRAIN("Terrain", "outdoors-v12","terrain"),
    HYBRID("Hybrid", "satellite-streets-v12","hybrid");

    private String title;
    private String mapboxString;
    private String googleString;

    MapType(String title, String mapboxString, String googleString) {
        this.title = title;
        this.mapboxString = mapboxString;
        this.googleString = googleString;
    }

    public MapType fromTitle(String title){
        for(MapType m : MapType.values()){
            if(m.title.equals(title)){
                return m;
            }
        }
        throw new IllegalArgumentException("Unexpected Map Type: "+title);
    }

    public String getMapboxString() {
        return mapboxString;
    }

    public String getGoogleString() {
        return googleString;
    }
}
