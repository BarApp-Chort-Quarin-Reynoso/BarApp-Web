package com.barapp.web.business.service;

import com.barapp.web.model.Restaurante;

import java.io.InputStream;

public interface RestauranteService extends BaseService<Restaurante> {

    String saveLogo(InputStream inputStream, String id, String contentType);

    String savePortada(InputStream inputStream, String id, String contentType);
}
