package com.barapp.web.views.signup.formularios;

import com.barapp.web.business.service.UbicacionService;
import com.barapp.web.model.Restaurante;
import com.barapp.web.views.utils.events.SiguienteFormularioEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class FormularioUbicacion extends VerticalLayout {

    H3 tituloFormularioH3;
    Paragraph textoParagraph;
    IFrame ubicacionIframe;

    HorizontalLayout botonesLayout;
    Button siguienteButton;
    Button volverButton;

    VerticalLayout formularioAnterior;

    Restaurante restaurante;

    UbicacionService ubicacionService;

    List<SiguienteFormularioEvent> siguienteFormularioListeners;

    @Autowired
    public FormularioUbicacion(UbicacionService ubicacionService, VerticalLayout formularioAnterior, Restaurante restaurante) {
        this.ubicacionService = ubicacionService;
        this.formularioAnterior = formularioAnterior;
        this.restaurante = restaurante;
        this.siguienteFormularioListeners = new ArrayList<>();
        configurarElementos();
        configurarLayout();
    }

    private void configurarElementos() {
        tituloFormularioH3 = new H3(getTranslation("views.registro.ubicacion"));
        textoParagraph = new Paragraph(getTranslation("views.registro.textoconfirmaubicacion"));
        textoParagraph.setWidthFull();

        ubicacionIframe = new IFrame();
        ubicacionIframe.setSrc(ubicacionService.getMapUrl(restaurante.getUbicacion()));
        ubicacionIframe.setSizeFull();

        siguienteButton = new Button(getTranslation("commons.siguiente"));
        siguienteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        siguienteButton.addClickListener(e -> {
            this.removeFromParent();
            for (SiguienteFormularioEvent listener : this.siguienteFormularioListeners) {
                listener.onSiguienteFormularioEvent(restaurante);
            }
        });

        volverButton = new Button(getTranslation("commons.volver"));
        volverButton.addClickListener(e -> {
            this.getParent().ifPresent(parent -> {
                ((VerticalLayout) parent).add(formularioAnterior);
                this.removeFromParent();
            });
        });
    }

    private void configurarLayout() {
        this.setPadding(false);
        this.setMargin(false);
        this.setId("registro-formulario-ubicacion");

        botonesLayout = new HorizontalLayout(volverButton, siguienteButton);
        botonesLayout.setId("registro-botones-layout");
        botonesLayout.setWidthFull();

        this.add(tituloFormularioH3, textoParagraph, ubicacionIframe, botonesLayout);
    }

    public void addSiguienteFormularioListener(SiguienteFormularioEvent listener) {
        this.siguienteFormularioListeners.add(listener);
    }

}
