package com.barapp.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
