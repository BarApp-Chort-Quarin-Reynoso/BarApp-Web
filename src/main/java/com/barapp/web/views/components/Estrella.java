package com.barapp.web.views.components;

import com.vaadin.flow.component.icon.SvgIcon;
import lombok.Getter;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class Estrella {
    @Getter
    private Double puntuacion;

    @Getter
    private SvgIcon icon;

    public Estrella() {
        puntuacion = 0.0;
        icon = LineAwesomeIcon.STAR.create();
    }

    public void setPuntuacion(Double puntuacion) {
        this.puntuacion = puntuacion;
        if (puntuacion <= 0.25) {
            icon = LineAwesomeIcon.STAR.create();
        } else if (puntuacion > 0.25 && puntuacion < 1) {
            icon = LineAwesomeIcon.STAR_HALF_ALT_SOLID.create();
        } else {
            icon = LineAwesomeIcon.STAR_SOLID.create();
        }
    }
}
