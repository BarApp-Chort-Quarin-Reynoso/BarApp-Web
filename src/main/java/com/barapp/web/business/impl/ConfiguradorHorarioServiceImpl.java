package com.barapp.web.business.impl;

import com.barapp.web.business.service.ConfiguradorHorarioService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ConfiguradorHorarioDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguradorHorarioServiceImpl extends BaseServiceImpl<ConfiguradorHorario> implements ConfiguradorHorarioService {

    private final ConfiguradorHorarioDao configuradorHorarioDao;

    public ConfiguradorHorarioServiceImpl(ConfiguradorHorarioDao configuradorHorarioDao) {
        this.configuradorHorarioDao = configuradorHorarioDao;
    }

    @Override
    public BaseDao<ConfiguradorHorario, ? extends BaseEntity> getDao() throws Exception {
        return configuradorHorarioDao;
    }

    @Override
    public List<ConfiguradorHorario> getAllByCorreoRestaurante(String correo) {
        return configuradorHorarioDao.getAllByCorreoRestaurante(correo);
    }

    @Override
    public List<ConfiguradorHorarioNoLaboral> getAllNoLaboralByCorreoRestaurante(String correo) {
        return configuradorHorarioDao.getAllNoLaboralByCorreoRestaurante(correo);
    }
}
