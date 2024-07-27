package com.barapp.web.business.service;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.HorarioPorRestaurante;

import java.util.Optional;

public interface HorarioPorRestauranteService extends BaseService<HorarioPorRestaurante> {
    Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo);
    void saveConfigurador(ConfiguradorHorario configuradorHorario, String id, String idHorariosPorRestaurante) throws Exception;
    void deleteConfigurador(String id, String idHorariosPorRestaurante) throws Exception;
}
