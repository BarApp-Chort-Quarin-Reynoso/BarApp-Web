package com.barapp.web.business.service;

import java.util.List;
import java.util.Optional;

import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioApp;

public interface UsuarioService extends BaseService<UsuarioApp> {
    Optional<UsuarioApp> getByMail(String mail);
    List<Restaurante> getFavoritos(String id);
}
