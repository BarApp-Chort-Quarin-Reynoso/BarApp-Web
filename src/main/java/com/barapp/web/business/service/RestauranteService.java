package com.barapp.web.business.service;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.model.*;
import com.barapp.web.utils.Tuple;
import com.google.type.LatLng;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestauranteService extends BaseService<Restaurante> {

    List<Restaurante> getAvailableOrPausedRestaurants();

    Restaurante getAvailableOrPausedRestaurantsWithDetail(String id);

    String saveLogo(InputStream inputStream, String id, String contentType);

    String savePortada(InputStream inputStream, String id, String contentType);

    void rechazarRestaurante(Restaurante restaurante);

    void aceptarRestaurante(Restaurante restaurante);

    void pausarRestaurante(Restaurante restaurante);

    void activarRestaurante(Restaurante restaurante);
    
    Optional<Restaurante> getByCorreo(String correo);

    String registrarRestaurante(Restaurante restaurante, UsuarioWeb usuario, ImageContainer logo, ImageContainer portada);

    String saveConFotos(Restaurante restaurante, ImageContainer logo, ImageContainer portada);

    Map<LocalDate, Map<String, HorarioConCapacidadDisponible>> horariosEnMesDisponiblesSegunDiaHoraActual(String correoRestaurante, YearMonth mesAnio);

    Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horariosEnMesDisponiblesSegunMesAnioConConfiguradorCoincidente(String correoRestaurante, YearMonth mesAnio);

    RestauranteUsuario addVistoRecientemente(String idRestaurante, RestauranteUsuario restaurante);

    List<String> addFavorito(String idRestaurante, RestauranteUsuario restauranteFavorito, String idDetalleUsuario);

    List<String> removeFavorito(String idRestaurante, String idUsuario, String idDetalleUsuario);

    List<Restaurante> getDestacados();

    List<Restaurante> getRestaurantesEnArea(LatLng northeast, LatLng southwest);

    List<Opinion> getAllOpiniones(String idRestaurante);
}
