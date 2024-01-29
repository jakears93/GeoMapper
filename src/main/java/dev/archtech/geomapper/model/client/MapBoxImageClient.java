package dev.archtech.geomapper.model.client;

import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.map.ImageResult;
import dev.archtech.geomapper.model.map.RequestParameters;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapBoxImageClient implements StaticMapClient{
    private static final String BASE_URL = "https://api.mapbox.com/styles/v1/mapbox/%s/static/%s+%s(%.4f,%.4f)/%.4f,%.4f,%d,0/%dx%d?access_token=%s";

    private final CloseableHttpClient client;

    public MapBoxImageClient() {
        this.client = HttpClients.createDefault();
    }

    @Override
    public ImageResult submitRequest(RequestParameters mapParameters) {
        HttpUriRequest request = buildRequest(mapParameters);
        AtomicBoolean repeat = new AtomicBoolean(false);
        ImageResult image = null;
        do {
            try{
                image = this.client.execute(request, classicHttpResponse -> {
                    if(classicHttpResponse.getCode() == 200){
                        repeat.set(false);
                        return new ImageResult(classicHttpResponse.getEntity().getContent().readAllBytes());
                    }
                    else if(classicHttpResponse.getCode() == 429){
                        System.out.println("Rate Limit Reached.  Waiting 60 seconds and trying again");
                        try {
                            Thread.sleep(60000);
                            repeat.set(true);
                            return null;
                        } catch (InterruptedException e) {
                            throw new RuntimeException("MapBox response code: "+classicHttpResponse.getCode()+ "\tReason: "+classicHttpResponse.getReasonPhrase());
                        }
                    }
                    else{
                        throw new IOException("MapBox response code: "+classicHttpResponse.getCode()+ "\tReason: "+classicHttpResponse.getReasonPhrase());
                    }
                });
            } catch (IOException | RuntimeException e) {
                System.err.println(e.getMessage());
                throw new FailedRequestException("Failed During Request To MapBox API");
            }
        } while(repeat.get());
        return image;
    }

    private HttpUriRequest buildRequest(RequestParameters mapParameters){
        String requestURL = String.format(BASE_URL,
                mapParameters.getMapType(),
                mapParameters.getMarkerSize(),
                mapParameters.getMarkerColour(),
                mapParameters.getLongitude(),
                mapParameters.getLatitude(),
                mapParameters.getLongitude(),
                mapParameters.getLatitude(),
                mapParameters.getZoom(),
                mapParameters.getWidth(),
                mapParameters.getHeight(),
                mapParameters.getApiKey());
        return new HttpGet(requestURL);
    }

    @Override
    public void close(){
        System.out.println("Closing MapBoxClient");
        try {
            this.client.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new FailedRequestException("Failed To Close MapBoxClient");
        }
    }
}
