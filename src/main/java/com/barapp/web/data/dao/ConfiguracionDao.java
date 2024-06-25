package com.barapp.web.data.dao;

import com.barapp.web.data.entities.ConfiguracionEntity;
import com.barapp.web.model.Configuracion;

public interface ConfiguracionDao extends BaseDao<Configuracion, ConfiguracionEntity> {
    Configuracion getRestaurantesConfig();
}
