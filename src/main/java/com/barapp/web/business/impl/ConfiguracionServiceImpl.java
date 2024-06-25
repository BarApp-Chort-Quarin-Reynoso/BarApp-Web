package com.barapp.web.business.impl;

import com.barapp.web.business.service.ConfiguracionService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ConfiguracionDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.Configuracion;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionServiceImpl extends BaseServiceImpl<Configuracion> implements ConfiguracionService {
    private final ConfiguracionDao configuracionDao;

    public ConfiguracionServiceImpl(ConfiguracionDao configuracionDao) {
        this.configuracionDao = configuracionDao;
    }

    @Override
    public BaseDao<Configuracion, ? extends BaseEntity> getDao() {
        return configuracionDao;
    }

    @Override
    public Configuracion getRestaurantesConfig() {
        return configuracionDao.getRestaurantesConfig();
    }
}
