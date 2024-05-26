package com.barapp.web.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetalleRestaurante extends BaseModel {
    String id;
    Integer capacidadPorHorario;
    String descripcion;
//    List<Horarios> listaHorarios;
//    List<Opiniones> listaOpiniones;
    String menu;

    public DetalleRestaurante() {
        this.id = UUID.randomUUID().toString();
        this.capacidadPorHorario = 1;
        this.descripcion = "";
        this.menu = "";
    }
}
