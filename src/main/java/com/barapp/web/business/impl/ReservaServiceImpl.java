package com.barapp.web.business.impl;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ReservaDao;
import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.enums.EstadoReserva;
import com.barapp.web.utils.FormatUtils;
import com.google.cloud.firestore.Filter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class ReservaServiceImpl extends BaseServiceImpl<Reserva> implements ReservaService {

    private final ReservaDao reservaDao;

    public ReservaServiceImpl(ReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    @Override
    public BaseDao<Reserva, ReservaEntity> getDao() {
        return reservaDao;
    }

    @Override
    public List<Reserva> getReservasByUsuario(String idUsuario) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.equalTo("idUsuario", idUsuario));

            reservas.sort(Comparator.comparing(Reserva::getFecha));

            return reservas;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> getReservasByRestaurante(String idRestaurante) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.equalTo("idRestaurante", idRestaurante));
            return reservas;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> getReservasByRestauranteEstado(String idRestaurante, EstadoReserva estado) {
        try {
            return reservaDao.getFiltered(Filter.and(
                    Filter.equalTo("idRestaurante", idRestaurante),
                    Filter.equalTo("estado", estado.toString())
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> getReservasByEstado(String estado) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.equalTo("estado", estado));
            return reservas;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<LocalDate, List<Reserva>> getReservasPendientesPorMes(String idRestaurante, YearMonth mes) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.and(
                    Filter.equalTo("idRestaurante", idRestaurante),
                    Filter.equalTo("estado", EstadoReserva.PENDIENTE.toString()),
                    Filter.greaterThanOrEqualTo("fecha", FormatUtils.persistenceDateFormatter().format(mes.atDay(1))),
                    Filter.lessThanOrEqualTo("fecha", mes.atEndOfMonth().format(FormatUtils.persistenceDateFormatter()))
            ));

            Map<LocalDate, List<Reserva>> reservasPorDia = new LinkedHashMap<>();
            reservas.forEach(reserva -> {
                LocalDate fecha = reserva.getFecha();
                if (!reservasPorDia.containsKey(fecha)) {
                    reservasPorDia.put(fecha, new ArrayList<>());
                }

                reservasPorDia.get(fecha).add(reserva);
            });

            return reservasPorDia;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reserva updateEstado(String id, String estado) {
        try {
            Reserva reserva = this.get(id);
            reserva.setEstado(EstadoReserva.valueOf(estado));
            this.save(reserva, id);

            return reserva;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

}
