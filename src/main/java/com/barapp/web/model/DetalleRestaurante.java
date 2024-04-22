package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
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
