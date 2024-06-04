package com.barapp.web.data.converter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.barapp.web.data.entities.ReservaEntity;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioApp;

public class ReservaConverter implements BaseConverter<Reserva, ReservaEntity> {
    @Override
    public ReservaEntity toEntity(Reserva dto) {
      return ReservaEntity
        .builder()
          // .id(dto.getId()) See formatUtils
          .fecha(dto.getFecha().toString())
          .hora(dto.getHora().toString())
          .cantidadPersonas(dto.getCantidadPersonas())
          .idRestaurante(dto.getRestaurante().getId())
          .idUsuario(dto.getUsuario().getIdUsuario())
          .build();
    }

    @Override
    public Reserva toDto(ReservaEntity entity) {
      return Reserva
        .builder()
          // .id(entity.getId())
          .fecha(LocalDate.parse(entity.getFecha()))
          .hora(LocalTime.parse(entity.getHora()))
          .cantidadPersonas(entity.getCantidadPersonas())
          .restaurante(Restaurante
            .builder()
              .id(entity.getIdRestaurante())
              .logo(entity.getLogo())
              .nombre(entity.getNombre())
              .puntuacion(entity.getPuntuacion())
              // .tipoComida(entity.getTipoComida())
              .build())
          .usuario(UsuarioApp
            .builder()
              .idUsuario(entity.getIdUsuario())
              .build())
          .build();
    }
  
}
