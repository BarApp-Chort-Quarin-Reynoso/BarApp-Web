package com.barapp.web.data.dao;

import com.barapp.web.data.entities.DetalleUsuarioEntity;
import com.barapp.web.model.DetalleUsuario;

import java.util.List;

public interface DetalleUsuarioDao extends BaseDao<DetalleUsuario, DetalleUsuarioEntity> {
    DetalleUsuario updateBusquedasRecientes(String id, List<String> busquedasRecientes);

    List<String> addFavorito(String idUsuario, String idRestaurante);

    List<String> removeFavorito(String idUsuario, String idRestaurante);
}
