package com.barapp.web.views;

import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;

import jakarta.annotation.security.RolesAllowed;

@SuppressWarnings("serial")
@PageTitle("Bar no habilitado")
@Route(value = "bar-no-habilitado", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class BarRechazadoView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;
    
    Span returnSpan = new Span();

    private final SecurityService securityService;

    public BarRechazadoView(SecurityService securityService) {
        this.securityService = securityService;

        setAlignItems(Alignment.CENTER);
        
        VerticalLayout textWrapper = new VerticalLayout();
        textWrapper.setWidth("50%");
        
        H3 title = new H3(getTranslation("views.barrechazado.estimadocliente"));
        
        Paragraph parrafo = new Paragraph(getTranslation("views.barrechazado.parrafo"));
        Paragraph atentamente = new Paragraph(getTranslation("views.barrechazado.atentamente"));
        atentamente.getElement().appendChild(ElementFactory.createBr());
        atentamente.add(getTranslation("views.barrechazado.elequipode"));
        Span barApp = new Span(getTranslation("commons.titulo"));
        barApp.addClassName("appname");
        barApp.addClassName(FontSize.LARGE);
        atentamente.add(barApp);
        
        textWrapper.add(title, parrafo, atentamente);
        add(textWrapper);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        EstadoRestaurante estadoBar = securityService.getEstadoBar(event);
        if (estadoBar != null && !estadoBar.equals(EstadoRestaurante.RECHAZADO)) {
            event.forwardTo(InicioView.class);
        }
    }
    
    
}
