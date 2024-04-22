package com.barapp.web.data.converter;

import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.Rol;
import com.barapp.web.model.UsuarioWeb;

public class UsuarioWebConverter implements BaseConverter<UsuarioWeb, UsuarioWebEntity> {

    @Override
    public UsuarioWebEntity toEntity(UsuarioWeb dto) {
        return UsuarioWebEntity.builder()
                               .email(dto.getEmail())
                               .hashedPassword(dto.getHashedPassword())
                               .rol(dto.getRol().toString())
                               .build();
    }

    @Override
    public UsuarioWeb toDto(UsuarioWebEntity entity) {
        return UsuarioWeb.builder()
                            .email(entity.getEmail())
                            .hashedPassword(entity.getHashedPassword())
                            .rol(Rol.valueOf(entity.getRol()))
                            .build();
    }
}
