package com.barapp.web.model;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class DetalleUsuario extends BaseModel {
    List<String> busquedasRecientes;
    String mail;
    List<String> idsRestaurantesFavoritos;
    String telefono;

    public DetalleUsuario() {
        this.id = UUID.randomUUID().toString();
        this.busquedasRecientes = null;
        this.mail = "";
        this.idsRestaurantesFavoritos = null;
        this.telefono = "";
    }
}
