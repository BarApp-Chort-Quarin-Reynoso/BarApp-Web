package com.barapp.web.data.dao;

import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Reserva;

public interface ReservaDao extends BaseDao<Reserva, ReservaEntity> {
    void actualizarPorNuevaOpinion(Reserva reserva, String idOpinion) throws Exception;
}
