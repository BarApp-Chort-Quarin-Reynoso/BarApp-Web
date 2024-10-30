package com.barapp.web.data.dao;

import com.barapp.web.data.entities.EstadisticaEntity;
import com.barapp.web.model.Estadistica;

public interface EstadisticaDao extends BaseDao<Estadistica, EstadisticaEntity> {
    void sumarReservaConcretada(String idRestaurante);
}
