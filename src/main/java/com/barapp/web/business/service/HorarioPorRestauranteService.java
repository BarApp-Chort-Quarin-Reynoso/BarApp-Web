package com.barapp.web.business.service;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.HorarioPorRestaurante;
import com.barapp.web.model.Mesa;

import java.util.List;
import java.util.Optional;

public interface HorarioPorRestauranteService extends BaseService<HorarioPorRestaurante> {
    Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo);
    void saveConfigurador(ConfiguradorHorario configuradorHorario, String id, String idHorariosPorRestaurante) throws Exception;
    void deleteConfigurador(String id, String idHorariosPorRestaurante) throws Exception;
    void saveMesas(List<Mesa> mesas, String idHorariosPorRestaurante) throws Exception;
}
