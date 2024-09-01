package com.barapp.web.controllers;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.HorarioConCapacidadDisponible;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;
import com.google.type.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(value = "/api/restaurantes")
@CrossOrigin("*")
public class RestauranteRestController extends BaseController<Restaurante> {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestauranteService restauranteService;

    public RestauranteRestController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @Override
    public RestauranteService getService() {return restauranteService;}

    @Override
    @GetMapping()
    public ResponseEntity<List<Restaurante>> getAll(@RequestParam Map<String, String> allParams) {
        try {
            return new ResponseEntity<>(this.restauranteService.getAvailableOrPausedRestaurants(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{correo}/horarios")
    public ResponseEntity<Map<LocalDate, Map<String, HorarioConCapacidadDisponible>>> getHorarios(@PathVariable String correo, @RequestParam YearMonth mesAnio, @RequestParam(required = false) Integer cantMeses) {
        try {
            if (cantMeses == null)
                cantMeses = 1;
            Map<LocalDate, Map<String, HorarioConCapacidadDisponible>> horarios = new LinkedHashMap<>();
            for (int i = 0; i < cantMeses; i++) {
                YearMonth month = mesAnio.plusMonths(i);
                horarios.putAll(restauranteService.horariosEnMesDisponiblesSegunDiaHoraActual(correo, month));
            }
            return new ResponseEntity<>(horarios, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/favoritos")
    public ResponseEntity<List<String>> addFavorito(@PathVariable String id, @RequestParam String idDetalleUsuario, @RequestBody RestauranteUsuario restaurante) {
        try {
            return new ResponseEntity<>(this.restauranteService
                    .addFavorito(id, restaurante, idDetalleUsuario), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/favoritos")
    public ResponseEntity<List<String>> removeFavorito(@PathVariable String id, @RequestParam String idUsuario, @RequestParam String idDetalleUsuario) {
        try {
            return new ResponseEntity<>(this.restauranteService
                    .removeFavorito(id, idUsuario, idDetalleUsuario), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/vistos-recientemente")
    public ResponseEntity<RestauranteUsuario> addVistoRecientemente(@PathVariable String id, @RequestBody RestauranteUsuario restaurante) {
        try {
            return new ResponseEntity<>(this.restauranteService.addVistoRecientemente(id, restaurante), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<Restaurante>> getDestacados() {
        try {
            return new ResponseEntity<>(this.restauranteService.getDestacados(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cercanos")
    public ResponseEntity<List<Restaurante>> getRestaurantesEnArea(@RequestParam String neLat, @RequestParam String neLon, @RequestParam String swLat, @RequestParam String swLon) {
        try {
            logger.info("Busqueda area: ne: %s, %s sw: %s, %s".formatted(neLat, neLon, swLat, swLon));
            LatLng northeast = LatLng
                    .newBuilder()
                    .setLatitude(Double.parseDouble(neLat))
                    .setLongitude(Double.parseDouble(neLon))
                    .build();
            LatLng southwest = LatLng
                    .newBuilder()
                    .setLatitude(Double.parseDouble(swLat))
                    .setLongitude(Double.parseDouble(swLon))
                    .build();
            return new ResponseEntity<>(this.restauranteService
                    .getRestaurantesEnArea(northeast, southwest), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
