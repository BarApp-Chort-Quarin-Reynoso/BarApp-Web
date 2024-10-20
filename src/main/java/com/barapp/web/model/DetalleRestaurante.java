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
    @Builder.Default
    String idRestaurante = "";
    @Builder.Default
    String descripcion = "";
    @Builder.Default
    List<Opinion> opiniones = new ArrayList<>();
    @Builder.Default
    String menu = "";
    @Builder.Default
    Set<Mesa> capacidadTotal = new LinkedHashSet<>();
    @Builder.Default
    Map<String, CalificacionPromedio> caracteristicas = new LinkedHashMap<>();

    public DetalleRestaurante() {
        this.id = UUID.randomUUID().toString();
        this.descripcion = "";
        this.opiniones = new ArrayList<>();
        this.menu = "";
        this.capacidadTotal = new LinkedHashSet<>();
        this.caracteristicas = new LinkedHashMap<>();
    }
}
