package com.barapp.web.data.dao;

import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.UsuarioWeb;

import java.util.Optional;

public interface UsuarioWebDao extends BaseDao<UsuarioWeb, UsuarioWebEntity> {

    Optional<UsuarioWeb> findByEmail(String email);

}
