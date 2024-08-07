package com.barapp.web.data.converter;

import com.barapp.web.data.entities.RestauranteUsuarioEntity;
import com.barapp.web.model.RestauranteUsuario;
import com.barapp.web.model.Ubicacion;

public class RestauranteUsuarioConverter implements BaseConverter<RestauranteUsuario, RestauranteUsuarioEntity> {

    @Override
    public RestauranteUsuarioEntity toEntity(RestauranteUsuario dto) {
        return RestauranteUsuarioEntity
            .builder()
                .idUsuario(dto.getIdUsuario())
                .idRestaurante(dto.getIdRestaurante())
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .puntuacion(dto.getPuntuacion())
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
                .correo(entity.getCorreo())
                .puntuacion(entity.getPuntuacion())
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
