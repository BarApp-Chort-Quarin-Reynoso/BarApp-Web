package com.barapp.web.business.impl;

import com.barapp.web.business.service.EstadisticaService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.EstadisticaDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.Estadistica;
import com.google.cloud.firestore.Filter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadisticaServiceImpl extends BaseServiceImpl<Estadistica> implements EstadisticaService {
    private final EstadisticaDao dao;

    public EstadisticaServiceImpl(EstadisticaDao dao) {
        this.dao = dao;
    }

    @Override
    public BaseDao<Estadistica, ? extends BaseEntity> getDao() {
        return dao;
    }

    @Override
    public Optional<Estadistica> getByCorreoRestaurante(String correo) {
        try {
            return dao.getFiltered(Filter.equalTo("correo", correo)).stream().findAny();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sumarReservaConcretada(String idRestaurante) {
        try {
            dao.sumarReservaConcretada(idRestaurante);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
