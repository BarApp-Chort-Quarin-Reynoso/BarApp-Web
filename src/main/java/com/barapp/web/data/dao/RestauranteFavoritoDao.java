package com.barapp.web.data.dao;

import java.util.List;

import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.Restaurante;

public interface RestauranteFavoritoDao extends BaseDao<Restaurante, RestauranteEntity> {
    List<Restaurante> getByUserId(String userId);
}
