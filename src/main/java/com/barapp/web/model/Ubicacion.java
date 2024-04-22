package com.barapp.web.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ubicacion extends BaseModel {
    String calle;
    @Builder.Default
    Integer numero = 0;
    @Builder.Default
    Double latitud = 0.0;
    @Builder.Default
    Double longitud = 0.0;
    @Builder.Default
    String nombreLocalidad = "";
    @Builder.Default
    String nombreProvincia = "";
    @Builder.Default
    String nombrePais = "";

    public Ubicacion() {
        calle = "";
        numero = 0;
    }

    public String getFullFormatUbicacion() {
        return String.format("%s %d, %s, %s, %s", calle, numero, nombreLocalidad, nombreProvincia, nombrePais);
    }

    public String getDireccion() {
        return String.format("%s %d", calle, numero);
    }
}
