package dev.archtech.geomapper.model.client;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsApi;
import com.google.maps.StaticMapsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;
import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.map.RequestParameters;

import java.io.IOException;

public class GoogleMapClient implements StaticMapClient{
    private final GeoApiContext context;
    private static final int DEFAULT_QPS = 250;
    private static final Size DEFAULT_SIZE = new Size(400, 400);

    public GoogleMapClient(String apiKey, String secret) {
        if(secret == null || secret.isEmpty()) {
            this.context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .queryRateLimit(DEFAULT_QPS)
                    .disableRetries()
                    .build();
            System.out.println("Client Built in 'API KEY ONLY' Mode.  All Requests will be Unsigned");
        }
        else {
            this.context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .queryRateLimit(DEFAULT_QPS)
                    .enterpriseCredentials(null, secret)
                    .disableRetries()
                    .build();
            System.out.println("Client Built in 'Signing' Mode.  All Requests will be Signed");
        }
    }

    @Override
    public dev.archtech.geomapper.model.map.ImageResult submitRequest(RequestParameters mapParameters){
        StaticMapsRequest request = buildRequest(mapParameters);
        try {
            ImageResult result = request.await();
            return new dev.archtech.geomapper.model.map.ImageResult(result.imageData);
        } catch (ApiException | InterruptedException | IOException e) {
            System.err.println(e.getMessage());
            throw new FailedRequestException("Failed During Request To Google API");
        }
    }



    private StaticMapsRequest buildRequest(RequestParameters mapParameters){
        LatLng location = new LatLng(mapParameters.getLatitude(), mapParameters.getLongitude());
        StaticMapsRequest.Markers markers = new StaticMapsRequest.Markers();
        markers.addLocation(location);
        markers.color(mapParameters.getMarkerColour());
        StaticMapsRequest.Markers.MarkersSize markersSize;
        if(mapParameters.getMarkerSize().equals("tiny")){
            markersSize = StaticMapsRequest.Markers.MarkersSize.tiny;
        }
        else{
            markersSize = StaticMapsRequest.Markers.MarkersSize.normal;
        }
        markers.size(markersSize);

        StaticMapsRequest request = StaticMapsApi.newRequest(this.context, DEFAULT_SIZE);
        request.center(location);
        request.zoom(mapParameters.getZoom());
        request.maptype(StaticMapsRequest.StaticMapType.valueOf(mapParameters.getMapType()));
        request.markers(markers);
        return request;
    }

    @Override
    public void close(){
        try {
            System.out.println("Closing GoogleMapClient");
            this.context.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new FailedRequestException("Failed To Close GoogleMapClient");
        }
    }

}
