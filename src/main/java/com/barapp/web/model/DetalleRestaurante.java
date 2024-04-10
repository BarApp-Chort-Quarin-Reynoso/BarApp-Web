package com.barapp.web.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DetalleRestaurante extends BaseModel {
    Integer capacidadPorHorario;
    String descripcion;
    List<Horario> listaHorarios;
    List<Opinion> listaOpiniones;
    String menu;

    public DetalleRestaurante() {
        this.id = UUID.randomUUID().toString();
        this.capacidadPorHorario = 1;
        this.descripcion = "";
        this.listaHorarios = new ArrayList<>();
        this.listaOpiniones = new ArrayList<>();
        this.menu = "";
    }
}
