package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservaEntity extends BaseEntity {
    // Reserva
    String estado;
    Integer cantidadPersonas;
    String fecha;
    String motivoCancelacion;
    // Horario
    String horario;
    String tipoComida;
    // Usuario
    String idUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    // Restaurante
    String idRestaurante;
    String nombreRestaurante;
    Double puntuacion;
    String portada;
    String logo;
    // Ubicacion
    Integer numero;
    String calle;
    // Opinion
    String idOpinion;
}
