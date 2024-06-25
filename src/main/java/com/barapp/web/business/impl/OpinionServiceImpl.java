package com.barapp.web.business.impl;

import com.barapp.web.business.service.OpinionService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.OpinionDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.Opinion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpinionServiceImpl extends BaseServiceImpl<Opinion> implements OpinionService {

    private final OpinionDao opinionDao;

    public OpinionServiceImpl(OpinionDao opinionDao) {
        this.opinionDao = opinionDao;
    }

    @Override
    public BaseDao<Opinion, ? extends BaseEntity> getDao() {
        return opinionDao;
    }

    @Override
    public List<Opinion> getAllOpinionesByRestaurante(String idRestaurante) {
        return opinionDao.getOpinionesByRestaurante(idRestaurante);
    }

    @Override
    public List<Opinion> getOpinionesRecientesByRestaurante(String idRestaurante) {
        return opinionDao.getAllOpinionesRecientesByRestaurante(idRestaurante);
    }
}
