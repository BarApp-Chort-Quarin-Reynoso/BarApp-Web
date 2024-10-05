package com.barapp.web.views.components;

import com.barapp.web.model.Reserva;
import com.barapp.web.utils.FormatUtils;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ReservaCard extends VerticalLayout {

    public ReservaCard(Reserva reserva) {
        this.setSpacing(false);
        addClassNames("outlined-card", "reserva-card");
        setMaxWidth("300px");

        H4 nombre = new H4(reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido());
        Span horario = new Span(reserva.getHorario().getHorario().format(FormatUtils.timeFormatter()));
        Span cantidadPersonas = new Span(reserva.getCantidadPersonas() == 1
                ? getTranslation("views.inicio.reservas.cantidadpersonas.singular")
                : getTranslation("views.inicio.reservas.cantidadpersonas.plural", reserva.getCantidadPersonas()));

        add(nombre, horario, cantidadPersonas);
    }
}
