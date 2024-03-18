package com.barapp.web.data.converter;

import com.barapp.web.data.entities.UsuarioWebEntity;
import com.barapp.web.model.Rol;
import com.barapp.web.model.UsuarioWebDto;

public class UsuarioWebConverter implements BaseConverter<UsuarioWebDto, UsuarioWebEntity> {

    @Override
    public UsuarioWebEntity toEntity(UsuarioWebDto dto) {
	return UsuarioWebEntity.builder()
		.email(dto.getEmail())
		.hashedPassword(dto.getHashedPassword())
		.rol(dto.getRol().toString())
		.build();
    }

    @Override
    public UsuarioWebDto toDto(UsuarioWebEntity entity) {
	return UsuarioWebDto.builder()
		.email(entity.getEmail())
		.hashedPassword(entity.getHashedPassword())
		.rol(Rol.valueOf(entity.getRol()))
		.build();
    }
}
