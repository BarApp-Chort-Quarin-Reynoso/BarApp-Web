package com.barapp.web.views.testing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.business.service.UsuarioWebService;
import com.barapp.web.model.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.Rol;
import com.barapp.web.model.Ubicacion;
import com.barapp.web.model.UsuarioWeb;
import com.barapp.web.utils.TestConsts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@SuppressWarnings("serial")
@PageTitle("Fake View")
@Route(value = "fakeview")
@AnonymousAllowed
public class FakeView extends VerticalLayout implements HasUrlParameter<String> {
    
    private final UsuarioWebService usuarioWebService;
    private final RestauranteService restauranteService;

    Span returnSpan = new Span();

    public FakeView(UsuarioWebService usuarioWebService, RestauranteService restauranteService) {
        this.usuarioWebService = usuarioWebService;
        this.restauranteService = restauranteService;
        
        H1 title = new H1("This is a fake view");

        returnSpan.setId("return-span");

        add(title, returnSpan);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter != null) {
            List<String> queryParams = event.getLocation().getQueryParameters().getParameters("params");

            for (Method m : this.getClass().getDeclaredMethods()) {
                if (m.getName().equals(parameter) && m.getParameterCount() == queryParams.size()) {
                    try {
                        m.invoke(this, queryParams.toArray());
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
        }
    }

    private void execute(String method) {
        UI.getCurrent().getPage().executeJs(String.format("""
                  setTimeout(() => {
                    $0.$server.%s();
                  });
                """, method), this.getElement());
    }

    private void crearUsuarioBarConEstado(String username, String estadoString) {
        EstadoRestaurante estado = EstadoRestaurante.valueOf(estadoString);
        String correo = username + TestConsts.DOMINIO_CORREO_TEST;
        UsuarioWeb usuarioWeb = UsuarioWeb
            .builder()
                .email(correo)
                .hashedPassword("$2a$10$gzxhNfZ545XcHXyOoKxAq.f.wEoy7vqyXiOIKG5N7QXTSkJJ1zXNi")
                .rol(Rol.BAR)
                .build();
        Restaurante restaurante = Restaurante
            .builder()
                .nombre(username)
                .correo(correo)
                .cuit("99-99999999-9")
                .ubicacion(Ubicacion
                    .builder()
                        .calle("Ejemplo")
                        .numero(123)
                        .nombreLocalidad("Santa Fe")
                        .nombreProvincia("Santa Fe")
                        .nombrePais("Argentina")
                        .build())
                .estado(estado)
                .build();

        try {
            restauranteService.save(restaurante, restaurante.getId());
            usuarioWebService.save(usuarioWeb, usuarioWeb.getId());

            returnSpan.setText(correo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void eliminarUsuarioBar(String correo) {
        UsuarioWeb usuarioWeb = usuarioWebService.getByUsername(correo);
        Optional<Restaurante> restauranteOpt = restauranteService.getByCorreo(correo);

        try {
            usuarioWebService.delete(usuarioWeb.getId());
            if (restauranteOpt.isPresent()) {
                restauranteService.delete(restauranteOpt.get().getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
