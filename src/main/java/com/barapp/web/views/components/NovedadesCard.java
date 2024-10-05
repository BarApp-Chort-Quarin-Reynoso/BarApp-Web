package com.barapp.web.views.components;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.InputStream;

public class NovedadesCard extends HorizontalLayout {
    public NovedadesCard() {
        setPadding(false);
        setSpacing(false);
        addClassNames("outlined-card", "novedades-card");
        getStyle().setWidth("500px");

        Image imagen = new Image();
        imagen.addClassName("novedades-card_logo");
        InputStream imagenInputStream = getClass().getResourceAsStream("/META-INF.resources/images/ic_barapp.png");
        StreamResource imagenDefaultResource = new StreamResource("ic_barapp.png", () -> imagenInputStream);
        imagen.setSrc(imagenDefaultResource);

        H4 titulo = new H4(getTranslation("views.inicio.novedades.titulo"));
        Span texto = new Span(getTranslation("views.inicio.novedades.texto"));

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(true);
        contentLayout.setSpacing(false);
        contentLayout.addClassName(LumoUtility.Gap.SMALL);
        contentLayout.add(titulo, texto);

        add(imagen, contentLayout);
    }
}
