package com.barapp.web.data.dao;

import com.barapp.web.data.entities.DetalleRestauranteEntity;
import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Opinion;

import java.util.Map;

public interface DetalleRestauranteDao extends BaseDao<DetalleRestaurante, DetalleRestauranteEntity> {
    void actualizarCaracteristicas(String idDetalleRestaurante, Map<String, CalificacionPromedio> caracteristicas);

    void actualizarPorNuevaOpinion(DetalleRestaurante detalleRestaurante, Opinion opinion) throws Exception;
}
