package com.barapp.web.business.service;

import com.barapp.web.model.Ubicacion;
import com.vaadin.flow.internal.Pair;

public interface UbicacionService {

    String getMapUrl(Ubicacion ubicacion);

    Pair<Double, Double> getLatitudeLongitud(Ubicacion ubicacion);

    void setLatitudLongitud(Ubicacion ubicacion);
}
