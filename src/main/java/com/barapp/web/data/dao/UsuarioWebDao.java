package com.barapp.web.data.dao;

import java.util.Optional;

import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.UsuarioWebDto;

public interface UsuarioWebDao extends BaseDao<UsuarioWebDto, UsuarioWebEntity> {

    Optional<UsuarioWebDto> findByEmail(String email);

}
