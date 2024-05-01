package com.barapp.web.data.converter;

import com.barapp.web.data.entities.DetalleUsuarioEntity;
import com.barapp.web.model.DetalleUsuario;

public class DetalleUsuarioConverter implements BaseConverter<DetalleUsuario, DetalleUsuarioEntity> {

    @Override
    public DetalleUsuarioEntity toEntity(DetalleUsuario dto) {
        DetalleUsuarioEntity entity = DetalleUsuarioEntity.builder()
                                            .busquedasRecientes(dto.getBusquedasRecientes())
                                            .idRestaurantesFavoritos(dto.getIdRestaurantesFavoritos())
                                            .mail(dto.getMail())
                                            .telefono(dto.getTelefono())
                                            .build();
        return entity;
    }

    @Override
    public DetalleUsuario toDto(DetalleUsuarioEntity entity) {
        DetalleUsuario dto = DetalleUsuario.builder()
                                  .busquedasRecientes(entity.getBusquedasRecientes())
                                  .idRestaurantesFavoritos(entity.getIdRestaurantesFavoritos())
                                  .mail(entity.getMail())
                                  .telefono(entity.getTelefono())
                                  .build();
        return dto;
    }
}
