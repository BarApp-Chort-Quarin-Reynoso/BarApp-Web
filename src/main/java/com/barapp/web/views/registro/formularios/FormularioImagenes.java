package com.barapp.web.views.registro.formularios;

import com.barapp.web.model.Restaurante;
import com.barapp.web.views.utils.events.SiguienteFormularioEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FormularioImagenes extends VerticalLayout {

    H3 tituloFormularioH3;

    Paragraph seleccionaLogo;
    HorizontalLayout logoLayout;
    Upload logoUpload;
    MemoryBuffer logoBuffer;
    Image logoImage;
    Paragraph seleccionaPortada;
    Upload portadaUpload;
    MemoryBuffer portadaBuffer;
    Image portadaImage;

    HorizontalLayout botonesLayout;
    Button siguienteButton;
    Button volverButton;

    VerticalLayout formularioAnterior;

    Restaurante restaurante;

    List<SiguienteFormularioEvent> siguienteFormularioListeners;

    public FormularioImagenes(VerticalLayout formularioAnterior, Restaurante restaurante) {
        this.formularioAnterior = formularioAnterior;
        this.restaurante = restaurante;
        this.siguienteFormularioListeners = new ArrayList<>();
        configurarElementos();
        configurarLayout();
        configurarBinders();
    }

    private void configurarElementos() {
        tituloFormularioH3 = new H3(getTranslation("views.registro.imagenes"));

        seleccionaLogo = new Paragraph(getTranslation("views.registro.selecionalogo"));

        logoBuffer = new MemoryBuffer();
        logoUpload = new Upload(logoBuffer);
        logoUpload.setId("registro-upload-logo");
        logoUpload.setMaxFiles(1);
        logoUpload.setMaxFileSize(40960); // 40 KB
        logoUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        logoUpload.addSucceededListener(event -> {
            InputStream inputStream = logoBuffer.getInputStream();
            StreamResource streamResource = new StreamResource("", () -> inputStream);
            logoImage.setSrc(streamResource);
        });
        logoUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        logoImage = new Image();
        StreamResource avatarDefaultResource = new StreamResource("ic_barapp.png",
                () -> getClass().getResourceAsStream("/META-INF.resources/images/ic_barapp.png"));
        logoImage.setSrc(avatarDefaultResource);
        logoImage.setId("registro-logo-avatar");

        seleccionaPortada = new Paragraph(getTranslation("views.registro.selecionaportada"));

        portadaBuffer = new MemoryBuffer();
        portadaUpload = new Upload(portadaBuffer);
        portadaUpload.setWidthFull();
        portadaUpload.setMaxFiles(1);
        portadaUpload.setMaxFileSize(5242880); // 5 MB
        portadaUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        portadaUpload.addSucceededListener(event -> {
            InputStream inputStream = portadaBuffer.getInputStream();
            StreamResource streamResource = new StreamResource("", () -> inputStream);
            portadaImage.setSrc(streamResource);
        });
        portadaUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        portadaImage = new Image();
        StreamResource portadaDefaultResource = new StreamResource("portada_barapp.png",
                () -> getClass().getResourceAsStream("/META-INF.resources/images/portada_barapp.png"));
        portadaImage.setSrc(portadaDefaultResource);
        portadaImage.setId("registro-imagen-portada");

        siguienteButton = new Button(getTranslation("commons.siguiente"));
        siguienteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        siguienteButton.addClickListener(e -> {
            this.removeFromParent();
            for(SiguienteFormularioEvent listener : this.siguienteFormularioListeners) {
                restaurante.setLogo(logoImage.getSrc());
                restaurante.setPortada(portadaImage.getSrc());
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
        this.setId("registro-formulario-imagenes");

        HorizontalLayout avatarLayout = new HorizontalLayout(logoImage);
        avatarLayout.setId("registro-avatar-layout");
        logoLayout = new HorizontalLayout(logoUpload, avatarLayout);
        logoLayout.setId("registro-logo-layout");
        logoLayout.setWidthFull();

        HorizontalLayout portadaLayout = new HorizontalLayout(portadaImage);
        portadaLayout.setId("registro-portada-layout");


        botonesLayout = new HorizontalLayout(volverButton, siguienteButton);
        botonesLayout.setId("registro-botones-layout");
        botonesLayout.setWidthFull();

        this.add(tituloFormularioH3, seleccionaLogo, logoLayout, seleccionaPortada, portadaUpload, portadaLayout, botonesLayout);
    }

    private void configurarBinders() {

    }

    public void addSiguienteFormularioListener(SiguienteFormularioEvent listener) {
        this.siguienteFormularioListeners.add(listener);
    }
}
