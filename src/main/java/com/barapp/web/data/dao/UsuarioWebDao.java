package com.barapp.web.data.dao;

import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.UsuarioWebDto;

import java.util.Optional;

public interface UsuarioWebDao extends BaseDao<UsuarioWebDto, UsuarioWebEntity> {

    Optional<UsuarioWebDto> findByEmail(String email);

}
