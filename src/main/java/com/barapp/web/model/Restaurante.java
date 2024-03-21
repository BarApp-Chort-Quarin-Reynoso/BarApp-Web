package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Restaurante {
    String idRestaurante;
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
        this.idRestaurante = UUID.randomUUID().toString();
        nombre = "";
        correo = "";
        puntuacion = 0.0;
        portada = "";
        logo = "";
        telefono = "";
        cuit = "";
        ubicacion = new Ubicacion();
        idDetalleRestaurante = "";
        detalleRestaurante = Optional.empty();
    }

}
