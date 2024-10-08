package com.barapp.web.model;

import com.barapp.web.model.enums.EstadoRestaurante;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurante extends BaseModel {
    @Builder.Default
    String nombre = "";

    @Builder.Default
    String correo = "";

    @Builder.Default
    Double puntuacion = 0.0;

    @Builder.Default
    Integer cantidadOpiniones = 0;

    @Builder.Default
    String portada = "";

    @Builder.Default
    String logo = "";

    @Builder.Default
    String telefono = "";

    @Builder.Default
    String cuit = "";

    @Builder.Default
    Ubicacion ubicacion = new Ubicacion();

    @Builder.Default
    String idDetalleRestaurante = "";

    @Builder.Default
    DetalleRestaurante detalleRestaurante = new DetalleRestaurante();
    EstadoRestaurante estado;

    public Restaurante() {
        id = UUID.randomUUID().toString();
        ubicacion = new Ubicacion();
        detalleRestaurante = new DetalleRestaurante();
        idDetalleRestaurante = detalleRestaurante.getId();
        estado = null;
    }

}
