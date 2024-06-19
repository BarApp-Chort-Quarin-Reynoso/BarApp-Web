package com.barapp.web.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioApp extends BaseModel {
    private String nombre;
    private String apellido;
    private String idUsuario;
    private String idDetalleUsuario;
    private String foto;
    private DetalleUsuario detalleUsuario;

    @Override
    public String toString() {
        return "UsuarioDto [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + "]";
    }
}	
