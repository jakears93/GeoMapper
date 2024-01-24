package dev.archtech.geomapper.model.client;

import dev.archtech.geomapper.model.map.ImageResult;
import dev.archtech.geomapper.model.map.RequestParameters;

public interface StaticMapClient extends AutoCloseable {
    ImageResult submitRequest(RequestParameters mapParameters);
}
