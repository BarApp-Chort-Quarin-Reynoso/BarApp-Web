package com.barapp.web.views;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.security.SecurityService;
import com.barapp.web.views.components.NovedadesCard;
import com.barapp.web.views.components.ReservaCard;
import com.barapp.web.views.components.ReservasPausadasCard;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.MainElement;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.Theme;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

@PageTitle("Inicio")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class InicioView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final ReservaService reservaService;

    private final VerticalLayout contentLayout = new VerticalLayout();

    private final H2 tituloReservas = new H2(getTranslation("views.inicio.reservas"));

    private Restaurante restaurante;

    public InicioView(SecurityService securityService, RestauranteService restauranteService,
            ReservaService reservaService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.reservaService = reservaService;

        configureBasicUI();
    }

    private void configureBasicUI() {
        setPadding(false);
        setSpacing(false);

        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);
        contentLayout.getStyle().set("gap", "3rem");
        contentLayout.addClassName("inicio-view");

        MainElement mainElement = new MainElement();
        mainElement.addClassName("inicio-view");

        try {
            configurarLandingPage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        add(contentLayout, new BarappFooter());
    }

    private void configureNovedadesLayout() {
        NovedadesCard novedadesCard = new NovedadesCard();
        contentLayout.add(novedadesCard);
    }

    private void configureReservasLayout() {
        List<Reserva> reservas = reservaService.getReservasPendientesDeHoy(
                restaurante.getId(), 4);

        VerticalLayout reservasLayout = new VerticalLayout();
        reservasLayout.setWidthFull();
        reservasLayout.setPadding(false);
        reservasLayout.add(tituloReservas);

        if (!reservas.isEmpty()) {
            HorizontalLayout reservasCards = new HorizontalLayout();
            reservasCards.addClassName("reservas-container");
            reservas.stream().map(ReservaCard::new).forEach(reservasCards::add);

            SvgIcon icon = LineAwesomeIcon.SEARCH_SOLID.create();
            icon.getStyle().setWidth("var(--lumo-icon-size-l)");
            icon.getStyle().setHeight("var(--lumo-icon-size-l)");

            Button verTodasReservas = new Button();
            verTodasReservas.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            verTodasReservas.setIcon(icon);
            verTodasReservas.setTooltipText(getTranslation("views.inicio.reservas.vertodasreservas"));
            verTodasReservas.addClickListener(ce -> {
                UI.getCurrent().navigate(MisReservasView.class);
            });
            reservasCards.add(verTodasReservas);

            reservasLayout.add(reservasCards);
        } else {
            Span noReservas = new Span(getTranslation("views.inicio.reservas.noReservas"));
            reservasLayout.add(noReservas);
        }
        contentLayout.add(reservasLayout);
    }

    public void configureReservasPausadasLayout() {
        ReservasPausadasCard reservasPausadasCard = new ReservasPausadasCard();
        reservasPausadasCard.addReservasReanudadasListener(event -> {
            restauranteService.activarRestaurante(restaurante);
            reservasPausadasCard.setVisible(false);
        });
        contentLayout.add(reservasPausadasCard);
    }

    private void configurarLandingPage() throws FileNotFoundException {
        VerticalLayout landingLayout = new VerticalLayout();

        landingLayout.addClassName("landing-layout");

        InputStream htmlStream = getClass().getClassLoader().getResourceAsStream("landing.html");

        if (htmlStream == null) {
            throw new FileNotFoundException("No se pudo encontrar el archivo landing.html");
        }

        try {
            Html landing = new Html(htmlStream);
            landingLayout.add(landing);
            landingLayout.add(landing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        contentLayout.add(landingLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
