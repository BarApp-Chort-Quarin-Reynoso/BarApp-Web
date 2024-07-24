package com.barapp.web.business.service;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.model.Horario;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.UsuarioWeb;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestauranteService extends BaseService<Restaurante> {

    String saveLogo(InputStream inputStream, String id, String contentType);

    String savePortada(InputStream inputStream, String id, String contentType);

    void rechazarRestaurante(Restaurante restaurante);

    void aceptarRestaurante(Restaurante restaurante);
    
    Optional<Restaurante> getByCorreo(String correo);

    String saveConUsuario(Restaurante restaurante, UsuarioWeb usuario, ImageContainer logo, ImageContainer portada);

    String saveConFotos(Restaurante restaurante, ImageContainer logo, ImageContainer portada);

    Map<LocalDate, List<Horario>> horariosEnMesDisponiblesSegunDiaHoraActual(String correoRestaurante, YearMonth mesAnio);

    Optional<DetalleRestaurante> getRestaurantDetail(String id);

    RestauranteUsuario addVistoRecientemente(String idRestaurante, RestauranteUsuario restaurante);
    
    RestauranteUsuario addFavorito(String idRestaurante, RestauranteUsuario restauranteFavorito);

    Void removeFavorito(String idRestauranteFavorito);
}
