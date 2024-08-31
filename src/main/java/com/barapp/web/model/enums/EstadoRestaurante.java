package com.barapp.web.model.enums;

import lombok.Getter;

public enum EstadoRestaurante {
    ESPERANDO_HABILITACION("views.estadorestaurante.esperandohabilitacion"),
    HABILITADO("views.estadorestaurante.habilitado"),
    PAUSADO("views.estadorestaurante.pausado"),
    RECHAZADO("views.estadorestaurante.rechazado");

    @Getter
    private final String translationKey;

    private EstadoRestaurante(String translationKey) {
        this.translationKey = translationKey;
    }
}
