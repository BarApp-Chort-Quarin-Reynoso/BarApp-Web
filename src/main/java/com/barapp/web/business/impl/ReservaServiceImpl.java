package com.barapp.web.business.impl;

import com.barapp.web.business.MobileNotification;
import com.barapp.web.business.service.NotificationService;
import com.barapp.web.business.service.ReservaService;
import com.barapp.web.data.dao.BaseDao;
import com.barapp.web.data.dao.DetalleRestauranteDao;
import com.barapp.web.data.dao.OpinionDao;
import com.barapp.web.data.dao.ReservaDao;
import com.barapp.web.data.dao.RestauranteDao;
import com.barapp.web.data.dao.RestauranteFavoritoDao;
import com.barapp.web.data.dao.RestauranteVistoRecientementeDao;
import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
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
    private final OpinionDao opinionDao;
    private final RestauranteDao restauranteDao;
    private final RestauranteFavoritoDao restauranteFavoritoDao;
    private final RestauranteVistoRecientementeDao restauranteVistoRecientementeDao;
    private final DetalleRestauranteDao detalleRestauranteDao;

    public ReservaServiceImpl(ReservaDao reservaDao, NotificationService notificationService, OpinionDao opinionDao, RestauranteDao restauranteDao, RestauranteFavoritoDao restauranteFavoritoDao, RestauranteVistoRecientementeDao restauranteVistoRecientementeDao, DetalleRestauranteDao detalleRestauranteDao) {
        this.reservaDao = reservaDao;
        this.notificationService = notificationService;
        this.opinionDao = opinionDao;
        this.restauranteDao = restauranteDao;
        this.restauranteFavoritoDao = restauranteFavoritoDao;
        this.restauranteVistoRecientementeDao = restauranteVistoRecientementeDao;
        this.detalleRestauranteDao = detalleRestauranteDao;
    }

    @Override
    public BaseDao<Reserva, ReservaEntity> getDao() { return reservaDao; }

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
    public Reserva concretarReserva(String idReserva, String idUsuario, String idRestaurante) {
        try {
            Reserva reserva = reservaDao.get(idReserva);

            if (!reserva.getRestaurante().getId().equals(idRestaurante) ||
                !reserva.getUsuario().getId().equals(idUsuario)) {
                return reserva;
            }

            boolean sePuedeConcretar = LocalDateTime.now().isAfter(LocalDateTime.of(reserva.getFecha(),reserva.getHorario().getHorario()).minusMinutes(30));
            if (!sePuedeConcretar) {
                return reserva;
            }

            reserva.setEstado(EstadoReserva.CONCRETADA);
            this.save(reserva, idReserva);

            return reserva;
        } catch (Exception e) {
            System.out.println(e);
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
    public List<Reserva> getUltimasReservasPendiente(String idRestaurante, String idUsuario, int cantidadMax) {
        try {
            List<Reserva> reservas = reservaDao.getFiltered(Filter.and(
                    Filter.equalTo("idRestaurante", idRestaurante),
                    Filter.equalTo("idUsuario", idUsuario),
                    Filter.equalTo("estado", EstadoReserva.PENDIENTE.toString())
            ));

            return reservas.stream()
                    .sorted(Comparator.comparing(Reserva::getFecha).reversed())
                    .limit(cantidadMax)
                    .toList();
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

        logger.info("Se programaron {} notificaciones de reservas pendientes", count);
    }

    @Override
    public Opinion reviewOnBooking(String idReserva, Opinion opinion) {
        try {
            Reserva reserva = this.get(idReserva);
            Restaurante restaurante = restauranteDao.get(opinion.getIdRestaurante());
            DetalleRestaurante detalleRestaurante = detalleRestauranteDao.get(restaurante.getIdDetalleRestaurante());
            this.validateReviewAndBooking(reserva, opinion, detalleRestaurante);

            Integer cantidadOpinionesActual = restaurante.getCantidadOpiniones();
            Double puntuacionActual = restaurante.getPuntuacion();

            Integer nuevaCantidadOpiniones = cantidadOpinionesActual + 1;
            Double nuevaPuntuacionRestaurante = (puntuacionActual * cantidadOpinionesActual + opinion
                    .getNota()) / (cantidadOpinionesActual + 1);
            nuevaPuntuacionRestaurante = Math.round(nuevaPuntuacionRestaurante * 10.0) / 10.0;

            reservaDao.actualizarPorNuevaOpinion(reserva, opinion.getId());
            restauranteDao.actualizarPorNuevaOpinion(restaurante, opinion, nuevaCantidadOpiniones, nuevaPuntuacionRestaurante);
            detalleRestauranteDao.actualizarPorNuevaOpinion(detalleRestaurante, opinion);    

            this.save(reserva, idReserva);
            opinionDao.save(opinion);
            restauranteDao.save(restaurante, restaurante.getId());
            detalleRestauranteDao.save(detalleRestaurante, detalleRestaurante.getId());

            restauranteFavoritoDao.actualizarYGuardarPorNuevaOpinion(restaurante, opinion, nuevaCantidadOpiniones, nuevaPuntuacionRestaurante);
            restauranteVistoRecientementeDao.actualizarYGuardarPorNuevaOpinion(restaurante, opinion, nuevaCantidadOpiniones, nuevaPuntuacionRestaurante);

            return opinion;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reserva cancelarReserva(String id, String estado, String motivo) {
        try {
            EstadoReserva estadoReserva = EstadoReserva.valueOf(estado);
            Reserva reserva = this.get(id);
            reserva.setEstado(estadoReserva);
            reserva.setMotivoCancelacion(motivo);
            this.save(reserva, id);

            notificationService.cancelNotificacion(id);
            if (estadoReserva.equals(EstadoReserva.CANCELADA_BAR)) {
                notificationService.sendNotification(reserva.getUsuario(), getReservaCanceladaBarNotification(reserva));
            }

            return reserva;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private MobileNotification getReservaNotification(Reserva reserva) {
        return MobileNotification.builder()
                .id(reserva.getId())
                .title_key("notificacion_reserva_title")
                .title_args(List.of(reserva.getRestaurante().getNombre()))
                .body_key("notificacion_reserva_text")
                .body_args(List.of(reserva.getRestaurante().getNombre()))
                .image(reserva.getRestaurante().getLogo())
                .click_action("barapp.RESERVA_CERCANA")
                .data(Map.of("origen", "1", "idReserva", reserva.getId()))
                .build();
    }

    private MobileNotification getReservaCanceladaBarNotification(Reserva reserva) {
        return MobileNotification.builder()
                .id(reserva.getId())
                .title_key("notificacion_reserva_cancelada_bar_title")
                .body_key("notificacion_reserva_cancelada_bar_text")
                .body_args(List.of(reserva.getRestaurante().getNombre(), reserva.getMotivoCancelacion()))
                .image(reserva.getRestaurante().getLogo())
                .data(Map.of("origen", "1", "idReserva", reserva.getId()))
                .build();
    }

    private void validateReviewAndBooking(Reserva reserva, Opinion opinion, DetalleRestaurante detalleRestaurante) {
        if (reserva == null) {
            throw new RuntimeException("Reserva no encontrada");
        }

        if (!reserva.getEstado().equals(EstadoReserva.CONCRETADA)) {
            throw new RuntimeException("La reserva no se encuentra CONCRETADA");
        }

        if (reserva.getIdOpinion() != null) {
            throw new RuntimeException("La reserva ya tiene una opinion asociada");
        }

        if (opinion == null) {
            throw new RuntimeException("Opinion no encontrada");
        }

        if (opinion.getNota() == null) {
            throw new RuntimeException("La nota es requerida");
        }

        if (opinion.getNota() < 1 || opinion.getNota() > 5) {
            throw new RuntimeException("La nota debe ser un valor entre 1 y 5");
        }

        if (!opinion.getIdRestaurante().equals(reserva.getRestaurante().getId())) {
            throw new RuntimeException("La opinion no corresponde al restaurante de la reserva");
        }

        if (!opinion.getUsuario().getId().equals(reserva.getUsuario().getId())) {
            throw new RuntimeException("La opinion no corresponde al usuario de la reserva");
        }

        if (!detalleRestaurante.getCaracteristicas().keySet().containsAll(opinion.getCaracteristicas().keySet())) {
            throw new RuntimeException("La opinion tiene caracteristicas que el restaurante no tiene");
        }
    }
}
