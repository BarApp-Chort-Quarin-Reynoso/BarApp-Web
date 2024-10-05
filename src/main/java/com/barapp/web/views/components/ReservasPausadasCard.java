package com.barapp.web.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ReservasPausadasCard extends HorizontalLayout {
    private Button pausarReservasButton;

    public ReservasPausadasCard() {
        addClassName("reservas-pausadas-container");

        Icon atencionIcon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        atencionIcon.setClassName("atencion-icon");
        atencionIcon.setColor("#ff6868");
        Span estado = new Span(getTranslation("view.misreservas.reservaspausadas"));
        estado.setClassName("pausar-estado");
        Span limitacion = new Span(getTranslation("view.misreservas.clientesnopodran"));
        limitacion.setClassName("pausar-limitacion");
        HorizontalLayout pausaTextos = new HorizontalLayout(estado, limitacion);
        pausaTextos.setClassName("pausa-textos");
        HorizontalLayout pausaContainerContent = new HorizontalLayout(atencionIcon, pausaTextos);
        pausaContainerContent.setClassName("pausa-content");
        pausarReservasButton = new Button(getTranslation("view.misreservas.activarreservas"));
        pausarReservasButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        pausarReservasButton.setClassName("pausa-button");
        add(pausaContainerContent, pausarReservasButton);
    }

    public void addReservasReanudadasListener(ComponentEventListener<ClickEvent<Button>> listener) {
        pausarReservasButton.addClickListener(listener);
    }
}
