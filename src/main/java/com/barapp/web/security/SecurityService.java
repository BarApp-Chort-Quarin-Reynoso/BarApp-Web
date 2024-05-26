package com.barapp.web.security;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.views.BarEsperandoHabilitacionView;
import com.barapp.web.views.BarRechazadoView;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final RestauranteService restauranteService;

    public SecurityService(AuthenticationContext authenticationContext, RestauranteService restauranteService) {
        this.authenticationContext = authenticationContext;
        this.restauranteService = restauranteService;
    }

    public Optional<UserDetails> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class);
    }

    public boolean isAuthenticated() {
        return authenticationContext.isAuthenticated();
    }

    public void logout() {
        authenticationContext.logout();
    }

    public void corroborarEstadoBar(BeforeEnterEvent event) {
        getAuthenticatedUser().ifPresent(u -> {
            EstadoRestaurante estadoBar = (EstadoRestaurante) ComponentUtil.getData(event.getUI(), "estadoBar");

            if (estadoBar == null) {
                Optional<Restaurante> restOpt = restauranteService.getByCorreo(u.getUsername());
                if (restOpt.isEmpty()) return;

                estadoBar = restOpt.get().getEstado();
                ComponentUtil.setData(event.getUI(), "estadoBar", estadoBar);
            }

            if (estadoBar.equals(EstadoRestaurante.ESPERANDO_HABILITACION)) {
                event.forwardTo(BarEsperandoHabilitacionView.class);
            }

            if (estadoBar.equals(EstadoRestaurante.RECHAZADO)) {
                event.forwardTo(BarRechazadoView.class);
            }
        });
    }

    public EstadoRestaurante getEstadoBar(BeforeEnterEvent event) {
        Optional<UserDetails> userOpt = getAuthenticatedUser();
        if (userOpt.isPresent()) {
            EstadoRestaurante estadoBar = (EstadoRestaurante) ComponentUtil.getData(event.getUI(), "estadoBar");

            if (estadoBar == null) {
                Optional<Restaurante> restOpt = restauranteService.getByCorreo(userOpt.get().getUsername());
                if (restOpt.isEmpty()) return null;

                estadoBar = restOpt.get().getEstado();
                ComponentUtil.setData(event.getUI(), "estadoBar", estadoBar);
                return estadoBar;
            }
        }

        return null;
    }
}