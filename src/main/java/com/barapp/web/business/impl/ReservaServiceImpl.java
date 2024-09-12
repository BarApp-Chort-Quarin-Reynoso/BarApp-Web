package com.barapp.web.business.impl;

import com.barapp.web.business.MobileNotification;
import com.barapp.web.business.service.NotificationService;
import com.barapp.web.business.service.ReservaService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.ReservaDao;
import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.enums.EstadoReserva;
import com.barapp.web.utils.FormatUtils;
import com.google.cloud.firestore.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class ReservaServiceImpl extends BaseServiceImpl<Reserva> implements ReservaService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReservaDao reservaDao;
    private final NotificationService notificationService;

    public ReservaServiceImpl(ReservaDao reservaDao, NotificationService notificationService) {
        this.reservaDao = reservaDao;
        this.notificationService = notificationService;
    }

    @Override
    public BaseDao<Reserva, ReservaEntity> getDao() {
        return reservaDao;
    }

    @Override
    public String save(Reserva dto, String id) throws Exception {
        dto.setId(id);
        return this.save(dto);
    }

    @Override
    public String save(Reserva dto) throws Exception {
        String id = super.save(dto);

        if (notificationService.isNotificationScheduled(id)) {
            notificationService.cancelNotificacion(id);
        }

        notificationService.scheduleNotificacion(
                dto.getUsuario(),
                getReservaNotification(dto),
                dto.getFecha().atTime(dto.getHorario().getHorario()).minusMinutes(30)
        );

        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        super.delete(id);

        notificationService.cancelNotificacion(id);
    }

    @Override
    public List<Reserva> getReservasByUsuario(String idUsuario) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.equalTo("idUsuario", idUsuario));

            reservas.sort(Comparator.comparing(Reserva::getFecha));

            return reservas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> getReservasByRestaurante(String idRestaurante) {
        try {
            return reservaDao.getFiltered(Filter.equalTo("idRestaurante", idRestaurante));
        } catch (Exception e) {
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
            return reservaDao.getFiltered(Filter.equalTo("estado", estado));
        } catch (Exception e) {
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
            EstadoReserva estadoReserva = EstadoReserva.valueOf(estado);
            reserva.setEstado(estadoReserva);
            super.save(reserva, id);

            if (estadoReserva == EstadoReserva.CANCELADA_BAR || estadoReserva == EstadoReserva.CANCELADA_USUARIO) {
                notificationService.cancelNotificacion(id);
            }

            return reserva;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inicializarNotificaciones() {
        int count = 0;
        List<Reserva> reservas = this.getReservasByEstado(EstadoReserva.PENDIENTE.toString());
        for (Reserva reserva : reservas) {
            LocalDateTime horaNotificacion = reserva
                    .getFecha()
                    .atTime(reserva.getHorario().getHorario())
                    .minusMinutes(30);

            if (horaNotificacion.isBefore(LocalDateTime.now())) {
                continue;
            }
            MobileNotification notificacion = getReservaNotification(reserva);
            notificationService.scheduleNotificacion(
                    reserva.getUsuario(),
                    notificacion,
                    horaNotificacion
            );

            count++;
        }

        logger.info("Se inicializaron {} notificaciones de reservas pendientes", count);
    }

    private MobileNotification getReservaNotification(Reserva reserva) {
        return MobileNotification.builder()
                .id(reserva.getId())
                .title_loc_key("notificacion_reserva_title")
                .title_loc_args(List.of(reserva.getRestaurante().getNombre()))
                .body_loc_key("notificacion_reserva_text")
                .body_loc_args(List.of(reserva.getRestaurante().getNombre()))
                .image(reserva.getRestaurante().getLogo())
                .click_action("barapp.RESERVA_CERCANA")
                .data(Map.of("origen", "1", "idReserva", reserva.getId()))
                .build();
    }

}
