package com.barapp.web.data.dao;

import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;

public interface RestauranteDao extends BaseDao<Restaurante, RestauranteEntity> {
    String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuarioWeb, HorarioPorRestaurante horarios) throws Exception;

    void actualizarPorNuevaOpinion(Restaurante restaurante, Opinion opinion, Integer nuevaCantidadOpiniones, Double nuevaPuntuacion) throws Exception;
}
