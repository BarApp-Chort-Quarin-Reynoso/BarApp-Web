package com.barapp.web.data.dao;

import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;

public interface RestauranteDao extends BaseDao<Restaurante, RestauranteEntity> {
    String saveConUsuario(Restaurante restaurante, UsuarioWeb usuarioWeb) throws Exception;
}
