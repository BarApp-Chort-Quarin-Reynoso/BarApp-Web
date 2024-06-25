package com.barapp.web.business.service;

import com.barapp.web.model.Configuracion;

public interface ConfiguracionService extends BaseService<Configuracion> {
    Configuracion getRestaurantesConfig();
}
