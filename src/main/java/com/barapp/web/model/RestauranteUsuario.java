package com.barapp.web.model;

import com.barapp.web.model.enums.EstadoRestaurante;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestauranteUsuario extends BaseModel {

    @Builder.Default
    String idRestaurante = "";

    @Builder.Default
    String idUsuario = "";

    @Builder.Default
    String nombre = "";

    @Builder.Default
    String correo = "";

    @Builder.Default
    Double puntuacion = 0.0;

    @Builder.Default
    String logo = "";

    @Builder.Default
    String portada = "";

    Ubicacion ubicacion;

    @Builder.Default
    String idDetalleRestaurante = "";

    @Builder.Default
    String fechaGuardado = "";

    @Builder.Default
    DetalleRestaurante detalleRestaurante = new DetalleRestaurante();
    EstadoRestaurante estado;
}
