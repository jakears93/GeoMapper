package dev.archtech.geomapper.service;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsApi;
import com.google.maps.StaticMapsRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.Size;
import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.MapParameters;

import java.io.IOException;

public class StaticMapClient implements AutoCloseable{
    private GeoApiContext context;
    private static final int DEFAULT_QPS = 250;
    private static final Size DEFAULT_SIZE = new Size(400, 400);
    private static final String DEFAULT_MARKER_COLOR = "blue";

    public StaticMapClient(String apiKey, String secret) {
        if(secret.equals(null) || secret.isEmpty()) {
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

    public byte[] submitRequest(MapParameters mapParameters){
        StaticMapsRequest request = buildRequest(mapParameters);
        try {
            ImageResult result = request.await();
            return result.imageData;
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StaticMapsRequest buildRequest(MapParameters mapParameters){
        LatLng location = new LatLng(mapParameters.getLatitude(), mapParameters.getLongitude());
        StaticMapsRequest.Markers markers = new StaticMapsRequest.Markers();
        markers.addLocation(location);
        markers.color(DEFAULT_MARKER_COLOR);
        markers.size(StaticMapsRequest.Markers.MarkersSize.tiny);

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
            System.out.println("Closing StaticMapClient");
            this.context.close();
        } catch (IOException e) {
            throw new FailedRequestException("Failed To Close StaticMapClient");
        }
    }

}
