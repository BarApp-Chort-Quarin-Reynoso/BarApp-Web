package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UsuarioApp extends BaseModel {
    private String nombre;
    private String apellido;
    private String idUsuario;
    private String idDetalleUsuario;
    private String foto;

    @Override
    public String toString() {
        return "UsuarioDto [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + "]";
    }
}	
