package dev.archtech.geomapper.model.map;

import lombok.Data;

@Data
public class Marker {
    private MarkerName name;
    private MarkerColour colour;
    private Coordinates location;
}
