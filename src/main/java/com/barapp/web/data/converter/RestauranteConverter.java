package com.barapp.web.data.converter;

import com.barapp.web.data.entities.RestauranteEntity;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.Ubicacion;

public class RestauranteConverter implements BaseConverter<Restaurante, RestauranteEntity> {

    @Override
    public RestauranteEntity toEntity(Restaurante dto) {
        return RestauranteEntity
            .builder()
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .puntuacion(dto.getPuntuacion())
                .logo(dto.getLogo())
                .portada(dto.getPortada())
                .telefono(dto.getTelefono())
                .cuit(dto.getCuit())
                .idUbicacion(dto.getUbicacion().getId())
                .calle(dto.getUbicacion().getCalle())
                .numero(dto.getUbicacion().getNumero())
                .latitud(dto.getUbicacion().getLatitud())
                .longitud(dto.getUbicacion().getLongitud())
                .nombreLocalidad(dto.getUbicacion().getNombreLocalidad())
                .nombreProvincia(dto.getUbicacion().getNombreProvincia())
                .nombrePais(dto.getUbicacion().getNombrePais())
                .idDetalleRestaurante(dto.getIdDetalleRestaurante())
                .estado(dto.getEstado().toString())
                .build();
    }

    @Override
    public Restaurante toDto(RestauranteEntity entity) {
        return Restaurante
            .builder()
                .nombre(entity.getNombre())
                .correo(entity.getCorreo())
                .puntuacion(entity.getPuntuacion())
                .portada(entity.getPortada())
                .logo(entity.getLogo())
                .telefono(entity.getTelefono())
                .cuit(entity.getCuit())
                .ubicacion(Ubicacion
                    .builder()
                        .id(entity.getIdUbicacion())
                        .calle(entity.getCalle())
                        .numero(entity.getNumero())
                        .latitud(entity.getLatitud())
                        .longitud(entity.getLongitud())
                        .nombreLocalidad(entity.getNombreLocalidad())
                        .nombreProvincia(entity.getNombreProvincia())
                        .nombrePais(entity.getNombrePais())
                        .build())
                .idDetalleRestaurante(entity.getIdDetalleRestaurante())
                .estado(entity.getEstado() != null ? EstadoRestaurante.valueOf(entity.getEstado()) : null)
                .build();
    }

}
