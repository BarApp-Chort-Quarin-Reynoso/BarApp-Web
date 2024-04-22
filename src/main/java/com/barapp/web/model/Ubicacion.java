package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ubicacion extends BaseModel {
    String calle;
    Integer numero;
    Double latitud;
    Double longitud;
    String nombreLocalidad;
    String nombreProvincia;
    String nombrePais;

    public Ubicacion() {
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

    public String getDireccion() {
        return String.format("%s %d", calle, numero);
    }
}
