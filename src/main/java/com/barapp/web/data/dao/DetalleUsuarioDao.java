package com.barapp.web.data.dao;

import java.util.List;
import com.barapp.web.data.entities.DetalleUsuarioEntity;
import com.barapp.web.model.DetalleUsuario;

public interface DetalleUsuarioDao extends BaseDao<DetalleUsuario, DetalleUsuarioEntity>{
  DetalleUsuario updateRestaurantesFavoritos(String id, List<String> restaurantes);
}
