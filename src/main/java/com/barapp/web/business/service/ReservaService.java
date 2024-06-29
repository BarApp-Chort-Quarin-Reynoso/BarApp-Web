package com.barapp.web.business.service;

import com.barapp.web.model.Opinion;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.enums.EstadoReserva;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface ReservaService extends BaseService<Reserva> {
    List<Reserva> getReservasByUsuario(String idUsuario);

    List<Reserva> getReservasByRestaurante(String idRestaurante);

    List<Reserva> getReservasByRestauranteEstado(String idRestaurante, EstadoReserva estado);

    List<Reserva> getReservasByEstado(String estado);

    Map<LocalDate, List<Reserva>> getReservasPendientesPorMes(String idRestaurante, YearMonth mes);

    Reserva updateEstado(String idReserva, String estado);

    List<Reserva> getUltimasReservasPendiente(String idRestaurante, String idUsuario, int cantidadMax);

    void inicializarNotificaciones();

    Opinion reviewOnBooking(String idReserva, Opinion opinion);

    Reserva cancelarReserva(String idReserva, String estado, String motivo);

    Reserva concretarReserva(String idReserva, String idUsuario, String idRestaurante);
}
