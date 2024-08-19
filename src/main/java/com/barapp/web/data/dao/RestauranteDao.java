package com.barapp.web.data.dao;

import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.HorarioPorRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;
import com.google.type.LatLng;

import java.util.List;

public interface RestauranteDao extends BaseDao<Restaurante, RestauranteEntity> {
    String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuarioWeb, HorarioPorRestaurante horarios) throws Exception;
}
