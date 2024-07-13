package com.barapp.web.data.dao;

import java.util.List;

import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.RestauranteUsuario;

public interface RestauranteFavoritoDao extends BaseDao<RestauranteUsuario, RestauranteUsuarioEntity> {
    List<RestauranteUsuario> getByUserId(String userId);
}
