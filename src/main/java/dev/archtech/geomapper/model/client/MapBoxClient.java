package dev.archtech.geomapper.model.client;

import dev.archtech.geomapper.model.map.RequestParameters;

public class MapBoxClient implements StaticMapClient{
    @Override
    public byte[] submitRequest(RequestParameters mapParameters) {
        System.out.println("MapBox Client used");
        return new byte[0];
    }

    @Override
    public void close(){
        System.out.println("Closing MapBoxClient");
    }
}
