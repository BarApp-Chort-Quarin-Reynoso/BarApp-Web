package com.barapp.web.data.converter;

import com.barapp.web.data.entities.OpinionUsuarioEntity;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.UsuarioMobileDto;

import java.util.List;

public class OpinionConverter {

    public static OpinionUsuarioEntity toEntity(Opinion dto) {
        OpinionUsuarioEntity entity = OpinionUsuarioEntity.builder()
                                                          .idOpinion(dto.getId())
                                                          .comentario(dto.getComentario())
                                                          .nota(dto.getNota())
                                                          .idUsuario(dto.getUsuario().getIdUsuario())
                                                          .nombreUsuario(dto.getUsuario().getNombre())
                                                          .apellidoUsuario(dto.getUsuario().getApellido())
                                                          .fotoUsuario(dto.getUsuario().getFoto())
                                                          .idDetalleUsuario(dto.getUsuario().getIdDetalleUsuario())
                                                          .build();

        return entity;
    }

    public static Opinion toDto(OpinionUsuarioEntity entity) {
        Opinion opinion = Opinion.builder()
                                 .comentario(entity.getComentario())
                                 .nota(entity.getNota())
                                 .usuario(UsuarioMobileDto.builder()
                                                          .idUsuario(entity.getIdUsuario())
                                                          .nombre(entity.getNombreUsuario())
                                                          .apellido(entity.getApellidoUsuario())
                                                          .idDetalleUsuario(entity.getIdDetalleUsuario())
                                                          .foto(entity.getFotoUsuario())
                                                          .build())
                                 .build();

        opinion.setId(entity.getIdOpinion());

        return opinion;
    }

    public static List<OpinionUsuarioEntity> toEntityList(List<Opinion> listaOpiniones) {
        return listaOpiniones.stream().map(OpinionConverter::toEntity).toList();
    }

    public static List<Opinion> toModelList(List<OpinionUsuarioEntity> listaOpinionesEntity) {
        return listaOpinionesEntity.stream().map(OpinionConverter::toDto).toList();
    }
}
