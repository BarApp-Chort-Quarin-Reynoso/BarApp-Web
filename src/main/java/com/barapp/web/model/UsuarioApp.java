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
<<<<<<< HEAD:src/main/java/com/barapp/web/model/UsuarioApp.java
@SuperBuilder
public class UsuarioApp extends BaseModel {
=======
@Builder
public class UsuarioMobileDto extends BaseModel {
>>>>>>> 7cfdfa7 (Agregar menu al bar):src/main/java/com/barapp/web/model/UsuarioMobileDto.java
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
