package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservaEntity extends BaseEntity{
  boolean cancelada;
  Integer cantidadPersonas;
  String fecha;
  String horario;
  String idUsuario;
  String idRestaurante;
  String logo;
  String nombre;
  Integer numero;
  Double puntuacion;
  String tipoComida;
  String calle;
}
