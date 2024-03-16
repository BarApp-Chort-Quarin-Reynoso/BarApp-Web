package com.barapp.web.data.entities;

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
public class UsuarioEntity extends BaseEntity {
    private String nombre;
    private String apellido;
    private String idUsuario;
    private String idDetalleUsuario;
    private String foto;
}
