package com.barapp.web.business.impl;

import com.barapp.web.business.service.HorarioPorRestauranteService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.HorarioPorRestauranteDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.HorarioPorRestaurante;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HorarioPorRestauranteServiceImpl extends BaseServiceImpl<HorarioPorRestaurante> implements HorarioPorRestauranteService {
    private final HorarioPorRestauranteDao horarioPorRestauranteDao;

    public HorarioPorRestauranteServiceImpl(HorarioPorRestauranteDao horarioPorRestauranteDao) {
        this.horarioPorRestauranteDao = horarioPorRestauranteDao;
    }

    @Override
    public BaseDao<HorarioPorRestaurante, ? extends BaseEntity> getDao() {
        return horarioPorRestauranteDao;
    }

    @Override
    public Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo) {
        return horarioPorRestauranteDao.getByCorreoRestaurante(correo);
    }

    @Override
    public void saveConfigurador(ConfiguradorHorario configuradorHorario, String id, String idHorariosPorRestaurante) throws Exception {
        HorarioPorRestaurante horarioPorRestaurante = horarioPorRestauranteDao.get(idHorariosPorRestaurante);
        if (horarioPorRestaurante == null) {
            throw new Exception("El idHorariosPorRestaurante no existe");
        }

        horarioPorRestaurante.getConfiguradores().put(id, configuradorHorario);
        horarioPorRestauranteDao.save(horarioPorRestaurante);
    }

    @Override
    public void deleteConfigurador(String id, String idHorariosPorRestaurante) throws Exception {
        HorarioPorRestaurante horarioPorRestaurante = horarioPorRestauranteDao.get(idHorariosPorRestaurante);
        if (horarioPorRestaurante == null) {
            throw new Exception("El idHorariosPorRestaurante no existe");
        }

        horarioPorRestaurante.getConfiguradores().remove(id);
        horarioPorRestauranteDao.save(horarioPorRestaurante);
    }
}
