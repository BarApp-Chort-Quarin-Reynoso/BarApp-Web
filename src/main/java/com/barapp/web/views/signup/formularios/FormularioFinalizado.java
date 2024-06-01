package com.barapp.web.views.signup.formularios;

import com.barapp.web.model.Restaurante;
import com.barapp.web.views.utils.events.SiguienteFormularioEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class FormularioFinalizado extends VerticalLayout {

    H3 tituloFormularioH3;

    Paragraph textoFinalizarP1;
    Paragraph textoFinalizarP2;
    Paragraph textoFinalizarP3;
    Paragraph textoFinalizarP4;

    HorizontalLayout botonesLayout;
    Button confirmarButton;

    Restaurante restaurante;

    List<SiguienteFormularioEvent> siguienteFormularioListeners;

    public FormularioFinalizado(Restaurante restaurante) {
        this.restaurante = restaurante;
        this.siguienteFormularioListeners = new ArrayList<>();
        configurarElementos();
        configurarLayout();
    }

    private void configurarElementos() {
        tituloFormularioH3 = new H3(getTranslation("views.registro.finalizado"));
        textoFinalizarP1 = new Paragraph(getTranslation("views.registro.textofinalizado1"));
        textoFinalizarP2 = new Paragraph(getTranslation("views.registro.textofinalizado2"));
        textoFinalizarP3 = new Paragraph(getTranslation("views.registro.textofinalizado3"));
        textoFinalizarP4 = new Paragraph(getTranslation("views.registro.textofinalizado4"));

        confirmarButton = new Button(getTranslation("commons.confirmar"));
        confirmarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmarButton.addClickListener(e -> {
            for (SiguienteFormularioEvent listener : this.siguienteFormularioListeners) {
                listener.onSiguienteFormularioEvent(restaurante);
            }
        });
    }

    private void configurarLayout() {
        this.setPadding(false);
        this.setMargin(false);
        this.setId("registro-formulario-finalizado");

        botonesLayout = new HorizontalLayout(confirmarButton);
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        this.add(tituloFormularioH3, textoFinalizarP1, textoFinalizarP2, textoFinalizarP3, textoFinalizarP4, botonesLayout);
    }

    public void addSiguienteFormularioListener(SiguienteFormularioEvent listener) {
        this.siguienteFormularioListeners.add(listener);
    }
}
