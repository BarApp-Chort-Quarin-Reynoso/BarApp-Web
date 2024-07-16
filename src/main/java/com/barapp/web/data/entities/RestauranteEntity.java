package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestauranteEntity extends BaseEntity {
    String nombre;
    String correo;
    Double puntuacion;
    Integer cantidadOpiniones;
    String logo;
    String portada;
    String telefono;
    String cuit;

    String idUbicacion;
    String calle;
    Integer numero;
    Double latitud;
    Double longitud;
    String nombreLocalidad;
    String nombreProvincia;
    String nombrePais;

    String idDetalleRestaurante;
    String estado;
}
