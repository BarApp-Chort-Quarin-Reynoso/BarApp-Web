package com.barapp.web.data.converter;

import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.EstadoReserva;
import com.barapp.web.model.enums.TipoComida;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaConverter implements BaseConverter<Reserva, ReservaEntity> {
    @Override
    public ReservaEntity toEntity(Reserva dto) {
        return ReservaEntity
                .builder()
                .estado(dto.getEstado().toString())
                .cantidadPersonas(dto.getCantidadPersonas())
                .fecha(dto.getFecha().toString())
                .horario(dto.getHorario().getHorario().toString())
                .idRestaurante(dto.getRestaurante().getId())
                .idUsuario(dto.getUsuario().getId())
                .logo(dto.getRestaurante().getLogo())
                .portada(dto.getRestaurante().getPortada())
                .nombre(dto.getRestaurante().getNombre())
                .puntuacion(dto.getRestaurante().getPuntuacion())
                .numero(dto.getRestaurante().getUbicacion().getNumero())
                .calle(dto.getRestaurante().getUbicacion().getCalle())
                .idOpinion(dto.getIdOpinion())
                .tipoComida(dto.getHorario().getTipoComida().toString())
                .build();
    }

    @Override
    public Reserva toDto(ReservaEntity entity) {
        return Reserva
                .builder()
                .fecha(LocalDate.parse(entity.getFecha()))
                .horario(Horario
                        .builder()
                        .horario(LocalTime.parse(entity.getHorario()))
                        .tipoComida(TipoComida.valueOf(entity.getTipoComida()))
                        .build())
                .cantidadPersonas(entity.getCantidadPersonas())
                .estado(entity.getEstado() != null ? EstadoReserva.valueOf(entity.getEstado()) : null)
                .restaurante(Restaurante
                        .builder()
                        .id(entity.getIdRestaurante())
                        .logo(entity.getLogo())
                        .portada(entity.getPortada())
                        .nombre(entity.getNombre())
                        .puntuacion(entity.getPuntuacion())
                        .ubicacion(Ubicacion
                                .builder()
                                .numero(entity.getNumero())
                                .calle(entity.getCalle())
                                .build())
                        .build())
                .usuario(UsuarioApp
                        .builder()
                        .id(entity.getIdUsuario())
                        .build())
                .idOpinion(entity.getIdOpinion())
                .build();
    }

}
