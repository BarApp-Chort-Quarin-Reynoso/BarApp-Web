package com.barapp.web.data.dao;

import com.barapp.web.data.entities.OpinionEntity;
import com.barapp.web.model.Opinion;

import java.util.List;

public interface OpinionDao extends BaseDao<Opinion, OpinionEntity> {
    List<Opinion> getOpinionesByRestaurante(String idRestaurante);

    List<Opinion> getAllOpinionesRecientesByRestaurante(String idRestaurante);
}
