package com.barapp.web.views.login;

import java.io.InputStream;
import java.util.Optional;

import com.barapp.web.security.LoginListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@SuppressWarnings("serial")
@PageTitle("Login")
@Route(value = "login")
@AnonymousAllowed
public class LoginView extends Div implements BeforeEnterObserver {
    private final LoginForm login;
    private final LoginI18n i18n;

    @Autowired
    public LoginView(RestauranteService restauranteService, SecurityService securityService, LoginListener loginListener) {
        addClassName("login-view");
        setSizeFull();

        i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle(getTranslation("commons.titulo"));
        i18nForm.setUsername(getTranslation("views.login.email"));
        i18nForm.setPassword(getTranslation("views.login.contrasenia"));
        i18nForm.setSubmit(getTranslation("views.login.iniciarsesion"));
        i18nForm.setForgotPassword(getTranslation("views.login.olvidastetucontrasenia"));
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle(getTranslation("views.login.error.usuariocontraincorrectos.title"));
        i18nErrorMessage.setMessage(getTranslation("views.login.error.usuariocontraincorrectos.text"));
        i18nErrorMessage.setUsername(getTranslation("views.login.error.emailrequerido"));
        i18nErrorMessage.setPassword(getTranslation("views.login.error.contraseniarequerida"));
        i18n.setErrorMessage(i18nErrorMessage);

        login = new LoginForm();
        login.setAction("login");
        login.setI18n(i18n);
        login.addClassName("login-form");

        login.addLoginListener(e -> {
            if (securityService.getAuthenticatedUser().isPresent()) {
                Optional<Restaurante> restauranteOpt = restauranteService.getByCorreo(
                        securityService.getAuthenticatedUser().get().getUsername());

                if (restauranteOpt.isPresent()) {
                    Restaurante restaurante = restauranteOpt.get();
                    System.out.println(restaurante);
                    if (restaurante.getEstado().equals(EstadoRestaurante.ESPERANDO_HABILITACION)) {
                        UI.getCurrent().navigate(BarEsperandoHabilitacionView.class);
                    }
                }
            }
        });

        Div infoContainer = new Div();
        infoContainer.setClassName("info-container");
        Image imagen = new Image();
        imagen.setClassName("imagen");
        InputStream imagenInputStream = getClass().getResourceAsStream("/META-INF.resources/images/Restaurante.jpg");
        StreamResource imagenDefaultResource = new StreamResource("Restaurante.jpg", () -> imagenInputStream);
        imagen.setSrc(imagenDefaultResource);

        infoContainer.add(imagen);

        add(infoContainer, login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
