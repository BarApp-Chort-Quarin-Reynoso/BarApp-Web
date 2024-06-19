package com.barapp.web.data.entities;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity extends BaseEntity {
    private String nombre;
    private String apellido;
    private String idUsuario;
    private String idDetalleUsuario;
    private String foto;
    private List<String> busquedasRecientes;
    private List<String> idRestaurantesFavoritos;
    private String mail;
    private String telefono;
}
