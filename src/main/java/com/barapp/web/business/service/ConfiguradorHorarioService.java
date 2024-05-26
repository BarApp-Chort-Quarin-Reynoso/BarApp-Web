package com.barapp.web.business.service;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;

import java.util.List;

public interface ConfiguradorHorarioService extends BaseService<ConfiguradorHorario> {
    List<ConfiguradorHorario> getAllByCorreoRestaurante(String correo);
    List<ConfiguradorHorarioNoLaboral> getAllNoLaboralByCorreoRestaurante(String correo);
}
