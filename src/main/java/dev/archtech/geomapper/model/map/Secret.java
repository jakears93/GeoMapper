package dev.archtech.geomapper.model.map;

import lombok.Data;

@Data
public class Secret {
    private String apiKey;
    private String signature;
}
