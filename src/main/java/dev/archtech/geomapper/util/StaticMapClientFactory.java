package dev.archtech.geomapper.util;

import dev.archtech.geomapper.model.client.GoogleMapClient;
import dev.archtech.geomapper.model.client.MapBoxClient;
import dev.archtech.geomapper.model.client.StaticMapClient;
import dev.archtech.geomapper.model.map.MapApiEnum;
import dev.archtech.geomapper.model.map.RequestProperties;

public class StaticMapClientFactory {
    public static StaticMapClient createClient(RequestProperties properties){
        if(MapApiEnum.GOOGLE.equals(properties.getApiType())){
            return new GoogleMapClient(properties.getSecret().getApiKey(), properties.getSecret().getSignature());
        }
        else if(MapApiEnum.MAPBOX.equals(properties.getApiType())){
            return new MapBoxClient();
        }
        else{
            throw new IllegalArgumentException("Unexpected Map API submitted: "+properties.getMapType());
        }
    }
}
