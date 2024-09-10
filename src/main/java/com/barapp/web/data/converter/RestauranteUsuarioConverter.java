package com.barapp.web.data.converter;

import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.Ubicacion;
import com.barapp.web.model.enums.EstadoRestaurante;

public class RestauranteUsuarioConverter implements BaseConverter<RestauranteUsuario, RestauranteUsuarioEntity> {

    @Override
    public RestauranteUsuarioEntity toEntity(RestauranteUsuario dto) {
        return RestauranteUsuarioEntity
                .builder()
                .idUsuario(dto.getIdUsuario())
                .idRestaurante(dto.getIdRestaurante())
                .nombre(dto.getNombre())
                .estado(dto.getEstado().toString())
                .correo(dto.getCorreo())
                .puntuacion(dto.getPuntuacion())
                .cantidadOpiniones(dto.getCantidadOpiniones())
                .logo(dto.getLogo())
                .portada(dto.getPortada())
                .idUbicacion(dto.getUbicacion().getId())
                .calle(dto.getUbicacion().getCalle())
                .numero(dto.getUbicacion().getNumero())
                .latitud(dto.getUbicacion().getLatitud())
                .longitud(dto.getUbicacion().getLongitud())
                .nombreProvincia(dto.getUbicacion().getNombreProvincia())
                .nombrePais(dto.getUbicacion().getNombrePais())
                .idDetalleRestaurante(dto.getIdDetalleRestaurante())
                .fechaGuardado(dto.getFechaGuardado())
                .build();
    }

    @Override
    public RestauranteUsuario toDto(RestauranteUsuarioEntity entity) {
        return RestauranteUsuario
                .builder()
                .idUsuario(entity.getIdUsuario())
                .idRestaurante(entity.getIdRestaurante())
                .nombre(entity.getNombre())
                .estado(entity.getEstado() != null ? EstadoRestaurante.valueOf(entity.getEstado()) : null)
                .correo(entity.getCorreo())
                .puntuacion(entity.getPuntuacion())
                .cantidadOpiniones(entity.getCantidadOpiniones())
                .logo(entity.getLogo())
                .portada(entity.getPortada())
                .ubicacion(Ubicacion
                        .builder()
                        .id(entity.getIdUbicacion())
                        .calle(entity.getCalle())
                        .numero(entity.getNumero())
                        .latitud(entity.getLatitud())
                        .longitud(entity.getLongitud())
                        .nombreProvincia(entity.getNombreProvincia())
                        .nombrePais(entity.getNombrePais())
                        .build())
                .idDetalleRestaurante(entity.getIdDetalleRestaurante())
                .fechaGuardado(entity.getFechaGuardado())
                .build();
    }

}
