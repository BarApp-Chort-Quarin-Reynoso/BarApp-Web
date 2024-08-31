package com.barapp.web.data.dao;

import com.barapp.web.data.entities.HorarioPorRestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;

import java.util.Optional;

public interface HorarioPorRestauranteDao
        extends BaseDao<HorarioPorRestaurante, HorarioPorRestauranteEntity> {
    Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo);
}
