package com.barapp.web.data.converter;

import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.UsuarioDto;

public class UsuarioConverter implements BaseConverter<UsuarioDto, UsuarioEntity> {

    @Override
    public UsuarioEntity toEntity(UsuarioDto dto) {
	UsuarioEntity entity = UsuarioEntity.builder()
		.nombre(dto.getNombre())
		.apellido(dto.getApellido())
		.idUsuario(dto.getIdUsuario())
		.idDetalleUsuario(dto.getIdDetalleUsuario())
		.foto(dto.getFoto())
		.build();
	return entity;
    }

    @Override
    public UsuarioDto toDto(UsuarioEntity entity) {
	UsuarioDto dto = UsuarioDto.builder()
		.nombre(entity.getNombre())
		.apellido(entity.getApellido())
		.idUsuario(entity.getIdUsuario())
		.idDetalleUsuario(entity.getIdDetalleUsuario())
		.foto(entity.getFoto())
		.build();
	return dto;
    }
    
}
