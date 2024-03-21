package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Restaurante extends BaseModel {
    String nombre;
    String correo;
    Double puntuacion;
    String portada;
    String logo;
    String telefono;
    String cuit;
    Ubicacion ubicacion;
    String idDetalleRestaurante;
    Optional<DetalleRestaurante> detalleRestaurante;

    public Restaurante() {
        id = UUID.randomUUID().toString();
        nombre = "";
        correo = "";
        puntuacion = 0.0;
        portada = "";
        logo = "";
        telefono = "";
        cuit = "";
        ubicacion = new Ubicacion();
        detalleRestaurante = Optional.of(new DetalleRestaurante());
        idDetalleRestaurante = detalleRestaurante.get().getId();
    }

}
