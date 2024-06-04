package com.barapp.web.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.model.Reserva;

@RestController
@RequestMapping(value = "/api/reservas")
@CrossOrigin("*")
public class ReservaController extends BaseController<Reserva>{

  private final ReservaService reservaService;

  public ReservaController(ReservaService reservaService) {
    this.reservaService = reservaService;
  }

  @Override
  public ReservaService getService() {
    return reservaService;
  }

  // Get asociated to a user by id
  @RequestMapping(value = "/usuario/{idUsuario}")
  public List<Reserva> getReservasByUsuario(@PathVariable("idUsuario") String idUsuario) {
    return getService().getReservasByUsuario(idUsuario);
  }
  
}
