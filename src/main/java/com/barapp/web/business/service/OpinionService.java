package com.barapp.web.business.service;

import com.barapp.web.model.Opinion;

import java.util.List;

public interface OpinionService extends BaseService<Opinion> {
    List<Opinion> getAllOpinionesByRestaurante(String idRestaurante);

    List<Opinion> getOpinionesRecientesByRestaurante(String idRestaurante);
}
