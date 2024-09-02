package com.barapp.web.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetalleRestaurante extends BaseModel {
    String idRestaurante;
    String descripcion;
    List<Opinion> opiniones;
    String menu;
    Set<Mesa> capacidadTotal;
    Map<String, CalificacionPromedio> caracteristicas;

    public DetalleRestaurante() {
        this.id = UUID.randomUUID().toString();
        this.descripcion = "";
        this.opiniones = new ArrayList<>();
        this.menu = "";
        this.capacidadTotal = new LinkedHashSet<>();
        this.caracteristicas = new LinkedHashMap<>();
    }
}
