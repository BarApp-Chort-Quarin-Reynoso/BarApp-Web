package com.barapp.web.data.converter;

import com.barapp.web.data.entities.OpinionEntity;
import com.barapp.web.model.Horario;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.UsuarioApp;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.utils.FormatUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class OpinionConverter implements BaseConverter<Opinion, OpinionEntity> {
    public OpinionEntity toEntity(Opinion dto) {
        return OpinionEntity
            .builder()
                .comentario(dto.getComentario())
                .nota(dto.getNota())
                .idUsuario(dto.getUsuario().getId())
                .idRestaurante(dto.getIdRestaurante())
                .nombreUsuario(dto.getUsuario().getNombre())
                .apellidoUsuario(dto.getUsuario().getApellido())
                .foto(dto.getUsuario().getFoto())
                .fecha(dto.getFecha().format(FormatUtils.persistenceDateFormatter()))
                .caracteristicas(dto.getCaracteristicas())
                .horario(dto.getHorario().getHorario().toString())
                .tipoComida(dto.getHorario().getTipoComida().toString())
                .cantidadPersonas(dto.getCantidadPersonas())
                .build();
    }

    public Opinion toDto(OpinionEntity entity) {
        return Opinion
            .builder()
                .idRestaurante(entity.getIdRestaurante())
                .comentario(entity.getComentario())
                .nota(entity.getNota())
                .fecha(LocalDate.parse(entity.getFecha(), FormatUtils.persistenceDateFormatter()))
                .caracteristicas(entity.getCaracteristicas())
                .cantidadPersonas(entity.getCantidadPersonas())
                .horario(Horario
                    .builder()
                        .horario(LocalTime.parse(entity.getHorario()))
                        .tipoComida(TipoComida.valueOf(entity.getTipoComida()))
                        .build())
                .usuario(UsuarioApp
                    .builder()
                        .id(entity.getIdUsuario())
                        .nombre(entity.getNombreUsuario())
                        .apellido(entity.getApellidoUsuario())
                        .foto(entity.getFoto())
                        .build())
                .build();
    }
}
