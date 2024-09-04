package com.barapp.web.data.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity extends BaseEntity {
    private String nombre;
    private String apellido;
    private String idDetalleUsuario;
    private String foto;
    private List<String> fcmTokens;
}
