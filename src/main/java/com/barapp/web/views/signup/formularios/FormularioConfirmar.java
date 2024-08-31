package com.barapp.web.views.signup.formularios;

import com.barapp.web.model.Restaurante;
import com.barapp.web.views.utils.events.SiguienteFormularioEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class FormularioConfirmar extends VerticalLayout {

    H3 tituloFormularioH3;

    Paragraph confirmaDatosParagraph;
    TextField nombreBarTextfield;
    TextField direccionTextfield;
    TextField correoElectronicoTextfield;
    TextField telefonoTextfield;
    TextField cuitTextfield;
    Paragraph imagenesParagraph;
    HorizontalLayout imagenesLayout;
    Image logoImage;
    Image portadaImage;

    HorizontalLayout botonesLayout;
    Button registraBarButton;
    Button volverButton;

    VerticalLayout formularioAnterior;

    Binder<Restaurante> binder;
    Restaurante restaurante;

    List<SiguienteFormularioEvent> siguienteFormularioListeners;


    public FormularioConfirmar(VerticalLayout formularioAnterior, Restaurante restaurante) {
        this.formularioAnterior = formularioAnterior;
        this.restaurante = restaurante;
        binder = new Binder<>(Restaurante.class);
        binder.setBean(restaurante);
        this.siguienteFormularioListeners = new ArrayList<>();
        configurarElementos();
        configurarLayout();
        configurarBinders();
    }

    private void configurarElementos() {
        tituloFormularioH3 = new H3(getTranslation("views.registro.ultimopaso"));
        confirmaDatosParagraph = new Paragraph(getTranslation("views.registro.confirmadatos"));

        nombreBarTextfield = new TextField(getTranslation("views.registro.nombrebar"));
        direccionTextfield = new TextField(getTranslation("views.registro.direccion"));
        correoElectronicoTextfield = new TextField(getTranslation("views.registro.correo"));
        telefonoTextfield = new TextField(getTranslation("views.registro.telefono"));
        cuitTextfield = new TextField(getTranslation("views.registro.cuit"));
        nombreBarTextfield.setWidthFull();
        direccionTextfield.setWidthFull();
        correoElectronicoTextfield.setWidthFull();
        telefonoTextfield.setWidthFull();
        cuitTextfield.setWidthFull();
        nombreBarTextfield.setReadOnly(true);
        direccionTextfield.setReadOnly(true);
        correoElectronicoTextfield.setReadOnly(true);
        telefonoTextfield.setReadOnly(true);
        cuitTextfield.setReadOnly(true);

        imagenesParagraph = new Paragraph(getTranslation("views.registro.imagenes"));

        logoImage = new Image();
        logoImage.setSrc(restaurante.getLogo());
        logoImage.setId("registro-logo-avatar");

        portadaImage = new Image();
        portadaImage.setSrc(restaurante.getPortada());
        portadaImage.setId("registro-imagen-portada");
        portadaImage.setWidth("400px");
        portadaImage.setHeight("160px");

        registraBarButton = new Button(getTranslation("views.registro.registratubar"));
        registraBarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registraBarButton.addClickListener(e -> {
            this.removeFromParent();
            for (SiguienteFormularioEvent listener : siguienteFormularioListeners) {
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
        this.setId("registro-formulario-confirmar");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("600px", 2));

        formLayout.add(
                nombreBarTextfield, correoElectronicoTextfield, direccionTextfield, telefonoTextfield, cuitTextfield);
        formLayout.setColspan(direccionTextfield, 2);

        imagenesLayout = new HorizontalLayout(logoImage, portadaImage);
        imagenesLayout.setWidthFull();
        imagenesLayout.setAlignItems(Alignment.CENTER);
        imagenesLayout.setJustifyContentMode(JustifyContentMode.EVENLY);

        botonesLayout = new HorizontalLayout(volverButton, registraBarButton);
        botonesLayout.setId("registro-botones-layout");
        botonesLayout.setWidthFull();

        this.add(
                tituloFormularioH3, confirmaDatosParagraph, formLayout, imagenesParagraph, imagenesLayout,
                botonesLayout
        );
    }

    private void configurarBinders() {

        binder.forField(nombreBarTextfield).bind(Restaurante::getNombre, null);

        binder.forField(correoElectronicoTextfield).bind(Restaurante::getCorreo, null);

        binder
                .forField(direccionTextfield)
                .bind(restaurante -> restaurante.getUbicacion().getFullFormatUbicacion(), null);

        binder.forField(telefonoTextfield).bind(Restaurante::getTelefono, null);

        binder.forField(cuitTextfield).bind(Restaurante::getCuit, null);
    }

    public void addSiguienteFormularioListener(SiguienteFormularioEvent listener) {
        this.siguienteFormularioListeners.add(listener);
    }
}
