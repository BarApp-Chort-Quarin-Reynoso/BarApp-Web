package com.barapp.web.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto extends BaseModel {
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
