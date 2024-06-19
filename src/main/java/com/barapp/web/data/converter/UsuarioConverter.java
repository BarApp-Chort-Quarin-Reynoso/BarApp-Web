package com.barapp.web.data.converter;

import com.barapp.web.data.entities.UsuarioEntity;
import com.barapp.web.model.DetalleUsuario;
import com.barapp.web.model.UsuarioApp;

public class UsuarioConverter implements BaseConverter<UsuarioApp, UsuarioEntity> {

    @Override
    public UsuarioEntity toEntity(UsuarioApp dto) {
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
    public UsuarioApp toDto(UsuarioEntity entity) {
        UsuarioApp dto = UsuarioApp.builder()
                                   .nombre(entity.getNombre())
                                   .apellido(entity.getApellido())
                                   .idUsuario(entity.getIdUsuario())
                                   .idDetalleUsuario(entity.getIdDetalleUsuario())
                                   .foto(entity.getFoto())
                                   .detalleUsuario(DetalleUsuario.builder()
                                                  .busquedasRecientes(entity.getBusquedasRecientes())
                                                  .idRestaurantesFavoritos(entity.getIdRestaurantesFavoritos())
                                                  .mail(entity.getMail())
                                                  .telefono(entity.getTelefono())
                                                  .build())
                                   .build();
        return dto;
    }

}
