package com.barapp.web.business.impl;

import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.data.QueryParams;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.DetalleRestauranteDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Restaurante;
import com.google.cloud.firestore.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class DetalleRestauranteServiceImpl extends BaseServiceImpl<DetalleRestaurante> implements DetalleRestauranteService {

    private final DetalleRestauranteDao detalleRestauranteDao;

    @Autowired
    public DetalleRestauranteServiceImpl(DetalleRestauranteDao detalleRestauranteDao) {
        this.detalleRestauranteDao = detalleRestauranteDao;
    }

    @Override
    public BaseDao<DetalleRestaurante, ? extends BaseEntity> getDao() {
        return this.detalleRestauranteDao;
    }

    @Override
    public void actualizarCaracteristicas(String idDetalleRestaurante, Map<String, CalificacionPromedio> caracteristicas) {
        detalleRestauranteDao.actualizarCaracteristicas(idDetalleRestaurante, caracteristicas);
    }

    @Override
    public Optional<DetalleRestaurante> getByIdRestaurante(String idRestaurante) {
        try {
            QueryParams qp = new QueryParams();
            qp.addFilter(Filter.equalTo("idRestaurante", idRestaurante));

            return detalleRestauranteDao.getByParams(qp).stream().findFirst();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
