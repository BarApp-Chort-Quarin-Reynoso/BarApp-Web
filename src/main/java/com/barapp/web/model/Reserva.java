package com.barapp.web.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Reserva extends BaseModel{
  boolean cancelada;
  Integer cantidadPersonas;
  LocalDate fecha;
  LocalTime hora;
  UsuarioApp usuario;
  Restaurante restaurante;

  @Override
  public String toString() {
    return "Reserva [id=" + id + ", cancelada=" + cancelada + ", cantidadPersonas=" + cantidadPersonas + ", fecha=" + fecha + ", hora=" + hora + ", usuario=" + usuario + ", restaurante=" + restaurante + "]";
  }
}
