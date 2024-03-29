package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto extends BaseDto {
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
