package com.barapp.web.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsuarioApp extends BaseModel {
    private String nombre;
    private String apellido;
    private String idDetalleUsuario;
    private String foto;
    private DetalleUsuario detalleUsuario;
    private Set<String> fcmTokens;

    @Override
    public String toString() {
        return "UsuarioApp [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + "]";
    }
}
