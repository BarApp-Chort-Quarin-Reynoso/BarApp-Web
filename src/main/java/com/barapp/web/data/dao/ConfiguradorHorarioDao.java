package com.barapp.web.data.dao;

import com.barapp.web.data.entities.ConfiguradorHorarioEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;

import java.util.List;

public interface ConfiguradorHorarioDao extends BaseDao<ConfiguradorHorario, ConfiguradorHorarioEntity> {
    List<ConfiguradorHorario> getAllByCorreoRestaurante(String correo);
    List<ConfiguradorHorarioNoLaboral> getAllNoLaboralByCorreoRestaurante(String correo);
}
