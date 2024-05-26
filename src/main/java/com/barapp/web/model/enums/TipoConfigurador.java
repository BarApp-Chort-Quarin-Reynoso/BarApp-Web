package com.barapp.web.model.enums;

import lombok.Getter;

@Getter
public enum TipoConfigurador {
    NO_LABORAL("configurador.nolaboral", 1),
    DIA_ESPECIFICO("configurador.diaespecifico", 2),
    SEMANAL("configurador.semanal", 3);

    private final String translationKey;
    private final int orden;

    TipoConfigurador(String translationKey, int orden) {
        this.translationKey = translationKey;
        this.orden = orden;
    }
}
