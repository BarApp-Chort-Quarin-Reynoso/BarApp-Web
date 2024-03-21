package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ubicacion extends BaseModel {
    String id;
    String calle;
    Integer numero;
    Double latitud;
    Double longitud;
    String nombreLocalidad;
    String nombreProvincia;
    String nombrePais;

    public Ubicacion() {
        id = UUID.randomUUID().toString();
        calle = "";
        numero = 0;
        latitud = 0.0;
        longitud = 0.0;
        nombreLocalidad = "";
        nombreProvincia = "";
        nombrePais = "";
    }

    public String getFullFormatUbicacion() {
        return String.format("%s %d, %s, %s, %s", calle, numero, nombreLocalidad, nombreProvincia, nombrePais);
    }
}
