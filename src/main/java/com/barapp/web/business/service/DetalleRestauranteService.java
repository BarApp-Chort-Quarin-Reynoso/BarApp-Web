package com.barapp.web.business.service;

import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;

import java.util.Map;
import java.util.Optional;

public interface DetalleRestauranteService extends BaseService<DetalleRestaurante> {
    void actualizarCaracteristicas(String idDetalleRestaurante, Map<String, CalificacionPromedio> caracteristicas);
    Optional<DetalleRestaurante> getByIdRestaurante(String idRestaurante);
}
