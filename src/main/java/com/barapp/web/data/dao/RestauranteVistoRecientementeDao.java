package com.barapp.web.data.dao;

import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;

import java.util.List;

public interface RestauranteVistoRecientementeDao extends BaseDao<RestauranteUsuario, RestauranteUsuarioEntity> {
    List<RestauranteUsuario> getByUserId(String userId);

    void actualizarYGuardarPorNuevaOpinion(Restaurante restaurante, Opinion opinion, Integer nuevaCantidadOpiniones, Double nuevaPuntuacion) throws Exception;
}
