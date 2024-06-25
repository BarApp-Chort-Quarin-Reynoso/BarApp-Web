package com.barapp.web.views;

import com.barapp.web.security.SecurityService;
import com.barapp.web.views.components.Footer;
import com.barapp.web.views.components.MainElement;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@SuppressWarnings("serial")
@PageTitle("Inicio")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class InicioView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;
    
    public InicioView(SecurityService securityService) {
        this.securityService = securityService;

        this.setId("inicio-view");

        this.add(new MainElement(), new Footer());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
