package dev.archtech.geomapper.util;

import dev.archtech.geomapper.model.GPSRowData;
import dev.archtech.geomapper.model.map.MapApiEnum;
import dev.archtech.geomapper.model.map.RequestParameters;
import dev.archtech.geomapper.model.map.RequestProperties;

public class ParameterResolver {
    public static RequestParameters resolveParameters(RequestProperties properties, GPSRowData gpsRowData){
        RequestParameters parameters = new RequestParameters();
        //Common values
        parameters.setApiKey(properties.getSecret().getApiKey());
        parameters.setSignature(properties.getSecret().getSignature());
        parameters.setZoom(properties.getZoom().getLevel());

        //Unique Values
        parameters.setLongitude(gpsRowData.getCoordinates().getLongitude());
        parameters.setLatitude(gpsRowData.getCoordinates().getLatitude());

        //API Specific values
        if(MapApiEnum.GOOGLE.equals(properties.getApiType())){
            parameters.setMapType(properties.getMapType().getGoogleString());
            parameters.setMarkerSize(properties.getMarker().getSize().getGoogleSize());
            parameters.setMarkerColour(properties.getMarker().getColour().getColourString());

        }
        else if(MapApiEnum.MAPBOX.equals(properties.getApiType())){
            parameters.setMapType(properties.getMapType().getMapboxString());
            parameters.setMarkerSize(properties.getMarker().getSize().getMapboxSize());
            parameters.setMarkerColour(properties.getMarker().getColour().getHexCode());
        }
        else{
            throw new IllegalArgumentException("Invalid API Type Used: "+properties.getApiType().getValue());
        }

        return parameters;
    }
}
