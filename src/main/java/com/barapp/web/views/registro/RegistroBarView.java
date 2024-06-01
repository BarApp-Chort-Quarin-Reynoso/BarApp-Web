package com.barapp.web.views.registro;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.business.service.UbicacionService;
import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.model.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.Rol;
import com.barapp.web.model.UsuarioWeb;
import com.barapp.web.views.InicioView;
import com.barapp.web.views.registro.formularios.*;
import com.barapp.web.views.utils.components.CustomErrorWindow;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayInputStream;

@AnonymousAllowed
@PageTitle("Registro")
@Route(value = "registro")
public class RegistroBarView extends VerticalLayout {

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

    PasswordEncoder passwordEncoder;

    UbicacionService ubicacionService;
    UsuarioWebService usuarioWebService;
    RestauranteService restauranteService;

    @Autowired
    public RegistroBarView(UbicacionService ubicacionService, UsuarioWebService usuarioWebService, RestauranteService restauranteService, PasswordEncoder passwordEncoder) {
        this.binder = new Binder<>(Restaurante.class);
        this.usuarioWebService = usuarioWebService;
        this.ubicacionService = ubicacionService;
        this.restauranteService = restauranteService;
        this.passwordEncoder = passwordEncoder;
        configurarElementos();
        configurarLayout();
    }

    private void configurarElementos() {
        tituloH1 = new H1(getTranslation("commons.titulo"));
        tituloH1.setId("barapp-titulo");
        registroSubtituloH2 = new H2(getTranslation("views.registro.registratubar"));
        informacion = new Paragraph(getTranslation("views.registro.informacion"));

        formularioInformacionBasica = new FormularioInformacionBasica(this.usuarioWebService);
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

            // Guardar restaurante en base de datos
            // TODO: Crear excepcion personalizada
            restaurante.setEstado(EstadoRestaurante.ESPERANDO_HABILITACION);
            // Buscar ubicacion y pasar la info de latitud y longitud
            ubicacionService.setLatitudLongitud(restaurante.getUbicacion());

            // Crear usuario web e Image Containers con la info de las fotos a guardar
            UsuarioWeb usuarioWeb = new UsuarioWeb(restaurante.getCorreo(), passwordEncoder.encode(formularioInformacionBasica.getContrasenia()), Rol.BAR);
            ImageContainer logo = new ImageContainer(new ByteArrayInputStream(formularioImagenes.getLogoByteArray()), restaurante.getId(), formularioImagenes.getLogoMimeType());
            ImageContainer portada = new ImageContainer(new ByteArrayInputStream(formularioImagenes.getPortadaByteArray()), restaurante.getId(), formularioImagenes.getPortadaMimeType());
            try {
                restauranteService.saveConUsuario(restaurante, usuarioWeb, logo, portada);
            } catch (Exception e) {
                CustomErrorWindow.showError(e);
            }

            formularioFinalizado = new FormularioFinalizado(restaurante);
            baseLayout.add(formularioFinalizado);
            configurarListenersFormularioFinalizado();
        });
    }

    private void configurarListenersFormularioFinalizado() {
        formularioFinalizado.addSiguienteFormularioListener(restaurante -> {
            UI.getCurrent().navigate(InicioView.class);
        });

    }
}
