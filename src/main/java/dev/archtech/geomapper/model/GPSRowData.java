package dev.archtech.geomapper.model;

import dev.archtech.geomapper.model.map.Coordinates;
import lombok.Data;

@Data
public class GPSRowData {
    private int index;
    private String time;
    private Coordinates coordinates;
}
