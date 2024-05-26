package com.barapp.web.model.enums;

import lombok.Getter;

public enum TipoComida {
    DESAYUNO("commons.desayuno"),
    ALMUERZO("commons.almuerzo"),
    MERIENDA("commons.merienda"),
    CENA("commons.cena");

    @Getter
    private final String translationKey;

    TipoComida(String translationKey) {
        this.translationKey = translationKey;
    }
}
