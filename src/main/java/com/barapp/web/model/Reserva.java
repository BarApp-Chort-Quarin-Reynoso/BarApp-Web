package com.barapp.web.model;

import java.time.LocalDate;
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
  Horario horario;
  UsuarioApp usuario;
  Restaurante restaurante;

  @Override
  public String toString() {
    return "Reserva [id=" + id + ", cancelada=" + cancelada + ", cantidadPersonas=" + cantidadPersonas + ", fecha=" + fecha + ", horario=" + horario + ", usuario=" + usuario + ", restaurante=" + restaurante + "]";
  }
}
