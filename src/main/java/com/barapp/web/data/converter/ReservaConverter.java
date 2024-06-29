package com.barapp.web.data.converter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Horario;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.Ubicacion;
import com.barapp.web.model.UsuarioApp;
import com.barapp.web.model.enums.TipoComida;

public class ReservaConverter implements BaseConverter<Reserva, ReservaEntity> {
    @Override
    public ReservaEntity toEntity(Reserva dto) {
      return ReservaEntity
        .builder()
          .cancelada(dto.isCancelada())
          .cantidadPersonas(dto.getCantidadPersonas())
          .fecha(dto.getFecha().toString())
          .horario(dto.getHorario().getHorario().toString())
          .idRestaurante(dto.getRestaurante().getId())
          .idUsuario(dto.getUsuario().getId())
          .logo(dto.getRestaurante().getLogo())
          .nombre(dto.getRestaurante().getNombre())
          .puntuacion(dto.getRestaurante().getPuntuacion())
          .numero(dto.getRestaurante().getUbicacion().getNumero())
          .calle(dto.getRestaurante().getUbicacion().getCalle())
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
          .cancelada(entity.isCancelada())
          .restaurante(Restaurante
            .builder()
              .id(entity.getIdRestaurante())
              .logo(entity.getLogo())
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
              .idUsuario(entity.getIdUsuario())
              .build())
          .build();
    }
  
}
