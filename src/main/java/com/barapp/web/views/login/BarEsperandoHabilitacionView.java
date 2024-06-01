package com.barapp.web.views.login;

import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.views.InicioView;
import com.barapp.web.views.MainLayout;
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
@PageTitle("Bar en espera de habilitación")
@Route(value = "bar-en-espera-habilitacion", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class BarEsperandoHabilitacionView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;

    public BarEsperandoHabilitacionView(SecurityService securityService) {
        this.securityService = securityService;

        setAlignItems(Alignment.CENTER);
        
        VerticalLayout textWrapper = new VerticalLayout();
        textWrapper.setWidth("50%");
        
        H3 title = new H3(getTranslation("views.esperandohabilitacion.estimadocliente"));
        
        Paragraph parrafo1 = new Paragraph(getTranslation("views.esperandohabilitacion.parrafo1"));
        Paragraph parrafo2 = new Paragraph(getTranslation("views.esperandohabilitacion.parrafo2"));
        Paragraph atentamente = new Paragraph(getTranslation("views.esperandohabilitacion.atentamente"));
        atentamente.getElement().appendChild(ElementFactory.createBr());
        atentamente.add(getTranslation("views.esperandohabilitacion.elequipode"));
        Span barApp = new Span(getTranslation("commons.titulo"));
        barApp.addClassName("appname");
        barApp.addClassName(FontSize.LARGE);
        atentamente.add(barApp);
        
        textWrapper.add(title, parrafo1, parrafo2, atentamente);
        add(textWrapper);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        EstadoRestaurante estadoBar = securityService.getEstadoBar(event);
        if (estadoBar != null && !estadoBar.equals(EstadoRestaurante.ESPERANDO_HABILITACION)) {
            event.forwardTo(InicioView.class);
        }
    }
}
