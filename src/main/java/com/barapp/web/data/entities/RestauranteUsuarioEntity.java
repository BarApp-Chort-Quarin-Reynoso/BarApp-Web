package com.barapp.web.data.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestauranteUsuarioEntity extends BaseEntity {
    String idUsuario;
    String nombre;
    String correo;
    Double puntuacion;
    String logo;
    String portada;

    String idUbicacion;
    String calle;
    Integer numero;
    Double latitud;
    Double longitud;
    String nombreProvincia;
    String nombrePais;

    String idDetalleRestaurante;
    String idRestaurante;
    String fechaGuardado;
}
