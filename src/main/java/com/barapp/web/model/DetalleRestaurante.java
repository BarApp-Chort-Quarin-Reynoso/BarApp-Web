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
    String descripcion;
    List<Horario> listaHorarios;
    List<Opinion> listaOpiniones;
    String menu;
    Set<Mesa> capacidadTotal;

    public DetalleRestaurante() {
        this.id = UUID.randomUUID().toString();
        this.descripcion = "";
        this.listaHorarios = new ArrayList<>();
        this.listaOpiniones = new ArrayList<>();
        this.menu = "";
        this.capacidadTotal = new LinkedHashSet<>();
    }
}
