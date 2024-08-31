package com.barapp.web.model.enums;

import lombok.Getter;

public enum EstadoReserva {
    PENDIENTE("views.estadoreserva.pendiente"),
    NO_ASISTIO("views.estadoreserva.noasistio"),
    CONCRETADA("views.estadoreserva.concretada"),
    CANCELADA_BAR("views.estadoreserva.canceladabar"),
    CANCELADA_USUARIO("views.estadoreserva.canceladausuario");

    @Getter
    private final String translationKey;

    private EstadoReserva(String translationKey) {
        this.translationKey = translationKey;
    }
}
