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
                // Reserva
                .estado(dto.getEstado().toString())
                .cantidadPersonas(dto.getCantidadPersonas())
                .fecha(dto.getFecha().toString())
                .motivoCancelacion(dto.getMotivoCancelacion())
                // Horario
                .horario(dto.getHorario().getHorario().toString())
                .tipoComida(dto.getHorario().getTipoComida().toString())
                // Usuario
                .idUsuario(dto.getUsuario().getId())
                .nombreUsuario(dto.getUsuario().getNombre())
                .apellidoUsuario(dto.getUsuario().getApellido())
                // Restaurante
                .idRestaurante(dto.getRestaurante().getId())
                .nombreRestaurante(dto.getRestaurante().getNombre())
                .puntuacion(dto.getRestaurante().getPuntuacion())
                .portada(dto.getRestaurante().getPortada())
                .logo(dto.getRestaurante().getLogo())
                // Ubicación
                .numero(dto.getRestaurante().getUbicacion().getNumero())
                .calle(dto.getRestaurante().getUbicacion().getCalle())
                // Opinion
                .idOpinion(dto.getIdOpinion())
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
                .motivoCancelacion(entity.getMotivoCancelacion())
                .estado(entity.getEstado() != null ? EstadoReserva.valueOf(entity.getEstado()) : null)
                .restaurante(Restaurante
                        .builder()
                        .id(entity.getIdRestaurante())
                        .logo(entity.getLogo())
                        .portada(entity.getPortada())
                        .nombre(entity.getNombreRestaurante())
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
                        .nombre(entity.getNombreRestaurante())
                        .apellido(entity.getApellidoUsuario())
                        .build())
                .idOpinion(entity.getIdOpinion())
                .build();
    }

}
