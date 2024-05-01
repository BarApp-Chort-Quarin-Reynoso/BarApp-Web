package com.barapp.web.business.service;

import java.util.Optional;

import com.barapp.web.model.UsuarioApp;

public interface UsuarioService extends BaseService<UsuarioApp> {
    Optional<UsuarioApp> getByMail(String mail);
}
