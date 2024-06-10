package com.barapp.web.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Mesa {
    Integer cantidadDePersonasPorMesa;
    Integer cantidadMesas;

    public Mesa() {
        this.cantidadDePersonasPorMesa = 1;
        this.cantidadMesas = 1;
    }

    public Mesa(Mesa mesa) {
        this.cantidadDePersonasPorMesa = mesa.getCantidadDePersonasPorMesa();
        this.cantidadMesas = mesa.getCantidadMesas();
    }
}
