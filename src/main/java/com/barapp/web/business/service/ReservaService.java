package com.barapp.web.business.service;

import java.util.List;

import com.barapp.web.model.Reserva;

public interface ReservaService extends BaseService<Reserva> {
  List<Reserva> getReservasByUsuario(String idUsuario);
  List<Reserva> getReservasByRestaurante(String idRestaurante);
  List<Reserva> getReservasByEstado(String estado);
  void updateEstado(String idReserva, String estado);
}
