package com.barapp.web.business.impl;

import com.barapp.web.business.service.UbicacionService;
import com.barapp.web.config.ConfigClass;
import com.barapp.web.http.response.Geocoding;
import com.barapp.web.model.Ubicacion;
import com.google.gson.Gson;
import com.vaadin.flow.internal.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    public String getMapUrl(Ubicacion ubicacion) {
        return "https://www.google.com/maps/embed/v1/place?key="
                .concat(ConfigClass.getInstance().MAPS_API_KEY)
                .concat("&q=").concat(getUbicacionParameter(ubicacion));
    }

    public Pair<Double, Double> getLatitudeLongitud(Ubicacion ubicacion) {
        String query = "https://maps.googleapis.com/maps/api/geocode/json?address="
                .concat(getUbicacionParameter(ubicacion)).concat("&key=")
                .concat(ConfigClass.getInstance().MAPS_API_KEY);

        Gson gson = new Gson();
        HttpRequest getRequest = null;
        try {
            getRequest = HttpRequest.newBuilder().uri(new URI(query)).GET().build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

            Geocoding geocodingResponse = gson.fromJson(getResponse.body(), Geocoding.class);

            Pair<Double, Double> latLong = new Pair<>(geocodingResponse.results.get(0).geometry.location.lat, geocodingResponse.results.get(0).geometry.location.lng);

            return latLong;
        } catch (URISyntaxException | InterruptedException | IOException e) {
            return new Pair<>(0.0, 0.0);
        }
    }

    @Override
    public void setLatitudLongitud(Ubicacion ubicacion) {
        Pair<Double, Double> latLong = this.getLatitudeLongitud(ubicacion);
        ubicacion.setLatitud(latLong.getFirst());
        ubicacion.setLongitud(latLong.getSecond());
    }

    private String getUbicacionParameter(Ubicacion ubicacion) {
        String parameter = "";
        parameter += ubicacion.getCalle().concat(" ");
        parameter += String.valueOf(ubicacion.getNumero()).concat(",");
        parameter += ubicacion.getNombreLocalidad().concat(",");
        parameter += ubicacion.getNombreProvincia().concat(",");
        parameter += ubicacion.getNombrePais();

        return parameter.replace(" ", "+");
    }

}
