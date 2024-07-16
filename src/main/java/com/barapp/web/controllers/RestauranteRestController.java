package com.barapp.web.controllers;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Horario;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/api/restaurantes")
@CrossOrigin("*")
public class RestauranteRestController extends BaseController<Restaurante> {

  private final RestauranteService restauranteService;

  public RestauranteRestController(RestauranteService restauranteService) {
    this.restauranteService = restauranteService;
  }

  @Override
  public RestauranteService getService() {
    return restauranteService;
  }

  @GetMapping("/detalle/{id}")
  public ResponseEntity<DetalleRestaurante> getRestaurantDetail(@PathVariable String id) {
    try {
      Optional<DetalleRestaurante> detalleRestaurante = this.restauranteService.getRestaurantDetail(id);
      if (!detalleRestaurante.isPresent()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(detalleRestaurante.get(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{correo}/horarios")
  public ResponseEntity<Map<LocalDate, List<Horario>>> getHorarios(@PathVariable String correo, @RequestParam YearMonth mesAnio) {
    try {
      return new ResponseEntity<>(this.restauranteService.horariosEnMesDisponiblesSegunDiaHoraActual(correo, mesAnio), HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @PostMapping("/{id}/vistos-recientemente")
  public ResponseEntity<RestauranteUsuario> addVistoRecientemente(@PathVariable String id, @RequestBody RestauranteUsuarioEntity restauranteEntity) {
    try {
      return new ResponseEntity<>(this.restauranteService.addVistoRecientemente(id, restauranteEntity), HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}/vistos-recientemente")
  public ResponseEntity<RestauranteUsuario> removeVistoRecientemente(@PathVariable String id) {
    try {
      this.restauranteService.removeVistoRecientemente(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
