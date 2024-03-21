package com.barapp.web.views.registro;

import com.barapp.web.business.interfaces.UbicacionService;
import com.barapp.web.model.Restaurante;
import com.barapp.web.views.registro.formularios.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Registro")
@Route(value = "registro")
public class RegistroBar extends VerticalLayout {

    VerticalLayout topLayout;
    H1 tituloH1;
    H2 registroSubtituloH2;
    Paragraph informacion;

    VerticalLayout baseLayout;

    FormularioInformacionBasica formularioInformacionBasica;
    FormularioUbicacion formularioUbicacion;
    FormularioImagenes formularioImagenes;
    FormularioConfirmar formularioConfirmar;
    FormularioFinalizado formularioFinalizado;

    Binder<Restaurante> binder;

    UbicacionService ubicacionService;

    @Autowired
    public RegistroBar(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
        this.binder = new Binder<>(Restaurante.class);
        configurarElementos();
        configurarLayout();
    }

    private void configurarElementos() {
        tituloH1 = new H1(getTranslation("commons.titulo"));
        tituloH1.setId("barapp-titulo");
        registroSubtituloH2 = new H2(getTranslation("views.registro.registratubar"));
        informacion = new Paragraph(getTranslation("views.registro.informacion"));

        formularioInformacionBasica = new FormularioInformacionBasica(binder);
        configurarListenersFormularioInformacionBasica();
    }

    private void configurarLayout() {
        this.setPadding(false);
        this.setMargin(false);
        this.setAlignItems(Alignment.CENTER);

        topLayout = new VerticalLayout(tituloH1, registroSubtituloH2, informacion);
        topLayout.setId("registro-top-layout");

        baseLayout = new VerticalLayout(formularioInformacionBasica);
        baseLayout.setId("registro-base-layout");

        this.add(topLayout, baseLayout);
    }

    private void configurarListenersFormularioInformacionBasica() {
        formularioInformacionBasica.addSiguienteFormularioListener(restaurante -> {
            formularioUbicacion = new FormularioUbicacion(this.ubicacionService, formularioInformacionBasica, restaurante);
            baseLayout.add(formularioUbicacion);
            configurarListenersFormularioUbicacion();
        });
    }

    private void configurarListenersFormularioUbicacion() {
        formularioUbicacion.addSiguienteFormularioListener(restaurante -> {
            this.formularioImagenes = new FormularioImagenes(formularioUbicacion, restaurante);
            baseLayout.add(formularioImagenes);
            configurarListenersFormularioImagenes();
        });
    }

    private void configurarListenersFormularioImagenes() {
        formularioImagenes.addSiguienteFormularioListener(restaurante -> {
            formularioConfirmar = new FormularioConfirmar(formularioImagenes, restaurante);
            baseLayout.add(formularioConfirmar);
            configurarListenersFormularioConfirmar();
        });
    }

    private void configurarListenersFormularioConfirmar() {
        formularioConfirmar.addSiguienteFormularioListener(restaurante -> {

        // Buscar ubicacion y pasar la info de latitud y longitud
//        ubicacionService.setLatitudLongitud(restaurante.getUbicacion());
        // TODO: Guardar usuario en Firebase Auth
        // TODO: Guardar informacion de restaurante en Firestore
        // TODO: Guardar imágenes en Firebase Storage

            formularioFinalizado = new FormularioFinalizado(restaurante);
            baseLayout.add(formularioFinalizado);
            configurarListenersFormularioFinalizado();
        });
    }

    private void configurarListenersFormularioFinalizado() {
        // TODO: Redireccionar a Login
//        UI.getCurrent().navigate()
    }
}
