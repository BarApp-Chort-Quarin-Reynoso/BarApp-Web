package com.barapp.web.business.impl;

import com.barapp.web.business.service.HorarioPorRestauranteService;
import com.barapp.web.business.service.ReservaService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.HorarioPorRestauranteDao;
import com.barapp.web.data.entities.BaseEntity;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.EstadoReserva;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class HorarioPorRestauranteServiceImpl extends BaseServiceImpl<HorarioPorRestaurante> implements HorarioPorRestauranteService {
    private final HorarioPorRestauranteDao horarioPorRestauranteDao;
    private final ReservaService reservaService;

    public HorarioPorRestauranteServiceImpl(HorarioPorRestauranteDao horarioPorRestauranteDao, ReservaService reservaService) {
        this.horarioPorRestauranteDao = horarioPorRestauranteDao;
        this.reservaService = reservaService;
    }

    @Override
    public BaseDao<HorarioPorRestaurante, ? extends BaseEntity> getDao() {
        return horarioPorRestauranteDao;
    }

    @Override
    public Optional<HorarioPorRestaurante> getByCorreoRestaurante(String correo) {
        Optional<HorarioPorRestaurante> horarioPorRestaurante = horarioPorRestauranteDao.getByCorreoRestaurante(correo);
        horarioPorRestaurante.ifPresent(horario -> {
            Map<String, ConfiguradorHorario> configuradoresOrdenados = new LinkedHashMap<>();

            horario.getConfiguradores().entrySet().stream().filter(ch -> {
                if (ch.getValue() instanceof ConfiguradorHorarioSemanal)
                    return true;
                if (ch.getValue() instanceof ConfiguradorHorarioNoLaboral)
                    return !((ConfiguradorHorarioNoLaboral) ch.getValue()).getFecha().isBefore(LocalDate.now());
                if (ch.getValue() instanceof ConfiguradorHorarioDiaEspecifico)
                    return !((ConfiguradorHorarioDiaEspecifico) ch.getValue()).getFecha().isBefore(LocalDate.now());
                return false;
            }).forEach(e -> configuradoresOrdenados.put(e.getKey(), e.getValue()));

            horario.setConfiguradores(configuradoresOrdenados);
        });

        return horarioPorRestaurante;
    }

    @Override
    public void saveConfigurador(ConfiguradorHorario configuradorHorario, String id, String idHorariosPorRestaurante) throws Exception {
        HorarioPorRestaurante horarioPorRestaurante = this.get(idHorariosPorRestaurante);
        if (horarioPorRestaurante == null) {
            throw new Exception("El idHorariosPorRestaurante no existe");
        }

        // Chequea primero si el configurador ya existe (se esta actualizando)
        boolean isUpdate = horarioPorRestaurante.getConfiguradores().containsKey(id);

        horarioPorRestaurante.getConfiguradores().put(id, configuradorHorario);

        if (!isUpdate) {
            Map<String, ConfiguradorHorario> configuradoresOrdenados = new LinkedHashMap<>();
            horarioPorRestaurante.getConfiguradores().put(id, configuradorHorario);
            horarioPorRestaurante.getConfiguradores().entrySet()
                    .stream()
                    .sorted(Comparator.comparing(e -> e.getValue().getTipo()))
                    .forEach(e -> configuradoresOrdenados.put(e.getKey(), e.getValue()));
            horarioPorRestaurante.setConfiguradores(configuradoresOrdenados);
        }

        this.save(horarioPorRestaurante);
    }

    @Override
    public void deleteConfigurador(String id, String idHorariosPorRestaurante) throws Exception {
        HorarioPorRestaurante horarioPorRestaurante = this.get(idHorariosPorRestaurante);
        if (horarioPorRestaurante == null) {
            throw new Exception("El idHorariosPorRestaurante no existe");
        }

        horarioPorRestaurante.getConfiguradores().remove(id);
        this.save(horarioPorRestaurante);
    }

    @Override
    public void saveMesas(List<Mesa> mesas, String idHorariosPorRestaurante) throws Exception {
        HorarioPorRestaurante horarioPorRestaurante = this.get(idHorariosPorRestaurante);
        if (horarioPorRestaurante == null) {
            throw new Exception("El idHorariosPorRestaurante no existe");
        }

        horarioPorRestaurante.setMesas(mesas);
        this.save(horarioPorRestaurante);
    }

    @Override
    public String save(HorarioPorRestaurante horario) throws Exception {
        List<Reserva> reservasPendientes = reservaService.getReservasByRestauranteEstado(
                horario.getIdRestaurante(), EstadoReserva.PENDIENTE);

        horario.filterReservasInvalidas(reservasPendientes)
                .forEach(reserva -> reservaService.updateEstado(
                        reserva.getId(), EstadoReserva.CANCELADA_BAR.toString()));

        return super.save(horario);
    }

    @Override
    public boolean validarHorariosYCapacidad(HorarioPorRestaurante horario) {
        List<Reserva> reservasPendientes = reservaService.getReservasByRestauranteEstado(
                horario.getIdRestaurante(), EstadoReserva.PENDIENTE);

        return !horario.hayReservasInvalidadas(reservasPendientes);
    }
}
