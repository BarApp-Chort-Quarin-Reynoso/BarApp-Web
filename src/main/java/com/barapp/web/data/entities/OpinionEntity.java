package com.barapp.web.data.entities;

import java.util.Map;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpinionEntity extends BaseEntity {
    String comentario;
    Double nota;
    String idUsuario;
    String idRestaurante;
    String nombreUsuario;
    String apellidoUsuario;
    String foto;
    String fecha;
    String horario;
    Integer cantidadPersonas;
    String tipoComida;
    Map<String, Integer> caracteristicas;
}
