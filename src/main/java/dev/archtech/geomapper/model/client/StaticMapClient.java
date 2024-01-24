package dev.archtech.geomapper.model.client;

import dev.archtech.geomapper.model.map.RequestParameters;

public interface StaticMapClient extends AutoCloseable {
    byte[] submitRequest(RequestParameters mapParameters);
}
