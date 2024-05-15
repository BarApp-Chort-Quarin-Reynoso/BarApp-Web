package com.barapp.web.business.service;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioWeb;

import java.io.InputStream;
import java.util.Optional;

public interface RestauranteService extends BaseService<Restaurante> {

    String saveLogo(InputStream inputStream, String id, String contentType);

    String savePortada(InputStream inputStream, String id, String contentType);

    void rechazarRestaurante(Restaurante restaurante);

    void aceptarRestaurante(Restaurante restaurante);
    
    Optional<Restaurante> getByCorreo(String correo);

    String saveConUsuario(Restaurante restaurante, UsuarioWeb usuario, ImageContainer logo, ImageContainer portada);

    String saveConFotos(Restaurante restaurante, ImageContainer logo, ImageContainer portada);
}
