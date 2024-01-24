package dev.archtech.geomapper.model.client;

import dev.archtech.geomapper.exception.FailedRequestException;
import dev.archtech.geomapper.model.map.ImageResult;
import dev.archtech.geomapper.model.map.RequestParameters;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;

public class MapBoxClient implements StaticMapClient{
    private static final String BASE_URL = "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/-122.3187,37.9395,10.83,0/300x200?access_token=YOUR_MAPBOX_ACCESS_TOKEN";

    private final CloseableHttpClient client;

    public MapBoxClient() {
        this.client = HttpClients.createDefault();
    }

    @Override
    public ImageResult submitRequest(RequestParameters mapParameters) {
        HttpUriRequest request = buildRequest(mapParameters);
        try{
            return this.client.execute(request, classicHttpResponse -> {
                if(classicHttpResponse.getCode() == 200){
                    return new ImageResult(classicHttpResponse.getEntity().getContent().readAllBytes());
                }
                else{
                    throw new IOException("MapBox response code: "+classicHttpResponse.getCode()+ "\tReason: "+classicHttpResponse.getReasonPhrase());
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new FailedRequestException("Failed During Request To MapBox API");
        }
    }

    private HttpUriRequest buildRequest(RequestParameters mapParameters){
        HttpUriRequest request = new HttpGet("https://www.google.com/images/branding/googlelogo/1x/googlelogo_light_color_272x92dp.png");
        return request;
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
