package com.barapp.web.controllers;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Reserva;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reservas")
@CrossOrigin("*")
public class  ReservaController extends BaseController<Reserva> {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Override
    public ReservaService getService() {return reservaService;}

    @RequestMapping(value = "/usuario/{idUsuario}")
    public List<Reserva> getReservasByUsuario(@PathVariable("idUsuario") String idUsuario) {
        return getService().getReservasByUsuario(idUsuario);
    }

    @RequestMapping(value = "/{idReserva}/concretar")
    public ResponseEntity<Reserva> concretarReserva(@PathVariable("idReserva") String idReserva, @RequestParam("idUsuario") String idUsuario, @RequestParam("idRestaurante") String idRestaurante) {
        try {
            return new ResponseEntity<>(getService().concretarReserva(idReserva, idUsuario, idRestaurante), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/{id}/estado")
    public ResponseEntity<Reserva> updateEstado(@PathVariable("id") String id, @RequestParam("estado") String estado) {
        try {
            return new ResponseEntity<>(this.reservaService.updateEstado(id, estado), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{id}/opinar")
    public ResponseEntity<Opinion> reviewOnBooking(@PathVariable("id") String id, @RequestBody Opinion opinion) {
        try {
            System.out.println("Opinion: " + opinion);
            return new ResponseEntity<>(this.reservaService.reviewOnBooking(id, opinion), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/ultimas")
    public ResponseEntity<List<Reserva>> getUltimasReservasPendientes(@RequestParam("idRestaurante") String idRestaurante, @RequestParam("idUsuario") String idUsuario, @RequestParam("cantidad") int cantidadMax) {
        try {
            return new ResponseEntity<>(
                    this.reservaService.getUltimasReservasPendiente(idRestaurante, idUsuario, cantidadMax), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
