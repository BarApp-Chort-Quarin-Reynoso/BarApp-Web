package com.barapp.web.business.service;

import java.util.List;
import java.util.Optional;

import com.barapp.web.model.DetalleUsuario;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.UsuarioApp;

public interface UsuarioService extends BaseService<UsuarioApp> {
    Optional<UsuarioApp> getByMail(String mail);
    List<RestauranteUsuario> getFavoritos(String id);
    List<RestauranteUsuario> getVistosRecientemente(String id);
    void updateFoto(String id, String foto);
    Optional<DetalleUsuario> getUserDetail(String id);
    Optional<DetalleUsuario> updateRestaurantesFavoritos(String id, List<String> restaurantes);
}
