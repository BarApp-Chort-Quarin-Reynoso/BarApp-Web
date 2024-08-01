package com.barapp.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.model.Reserva;

@RestController
@RequestMapping(value = "/api/reservas")
@CrossOrigin("*")
public class ReservaController extends BaseController<Reserva> {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Override
    public ReservaService getService() { return reservaService; }

    @RequestMapping(value = "/usuario/{idUsuario}")
    public List<Reserva> getReservasByUsuario(@PathVariable("idUsuario") String idUsuario) {
        return getService().getReservasByUsuario(idUsuario);
    }

    @PatchMapping(value = "/{id}/estado")
    public ResponseEntity<Reserva> updateEstado(@PathVariable("id") String id, @RequestParam("estado") String estado) {
        try {
            return new ResponseEntity<>(this.reservaService.updateEstado(id, estado), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
