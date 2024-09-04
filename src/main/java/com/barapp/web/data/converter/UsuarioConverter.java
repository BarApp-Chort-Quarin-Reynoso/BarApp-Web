package com.barapp.web.data.converter;

import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioApp;

import java.util.ArrayList;
import java.util.HashSet;

public class UsuarioConverter implements BaseConverter<UsuarioApp, UsuarioEntity> {

    @Override
    public UsuarioEntity toEntity(UsuarioApp dto) {
        UsuarioEntity entity = UsuarioEntity.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .idDetalleUsuario(dto.getIdDetalleUsuario())
                .foto(dto.getFoto())
                .fcmTokens(new ArrayList<>(dto.getFcmTokens()))
                .build();
        return entity;
    }

    @Override
    public UsuarioApp toDto(UsuarioEntity entity) {
        UsuarioApp dto = UsuarioApp.builder()
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .idDetalleUsuario(entity.getIdDetalleUsuario())
                .foto(entity.getFoto())
                .fcmTokens(entity.getFcmTokens() != null ? new HashSet<>(entity.getFcmTokens()) : new HashSet<>())
                .build();
        return dto;
    }

}
