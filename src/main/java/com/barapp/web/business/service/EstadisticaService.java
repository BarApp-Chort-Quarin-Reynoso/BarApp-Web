package com.barapp.web.business.service;

import com.barapp.web.model.Estadistica;

import java.util.Optional;

public interface EstadisticaService extends BaseService<Estadistica> {
    Optional<Estadistica> getByCorreoRestaurante(String correo);

    void sumarReservaConcretada(String idRestaurante);
}
