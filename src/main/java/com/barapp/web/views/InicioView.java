package com.barapp.web.views;

import com.barapp.web.security.SecurityService;
import com.barapp.web.views.components.BarappFooter;
import com.barapp.web.views.components.MainElement;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Inicio")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class InicioView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;
    
    public InicioView(SecurityService securityService) {
        this.securityService = securityService;

        setId("inicio-view");
        setPadding(false);
        setSpacing(false);

        add(new MainElement(), new BarappFooter());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
