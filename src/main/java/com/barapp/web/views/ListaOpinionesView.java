package com.barapp.web.views;

import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.business.service.OpinionService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.views.components.EstrellasPuntuacion;
import com.barapp.web.views.components.VisualizadorOpinion;
import com.barapp.web.views.dialogs.OpinionesDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@PageTitle("Mis opiniones")
@Route(value = "mis-opiniones", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class ListaOpinionesView extends HorizontalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final DetalleRestauranteService detalleRestauranteService;
    private final OpinionService opinionService;

    private Restaurante restaurante;
    private DetalleRestaurante detalleRestaurante;
    private List<Opinion> opinionesRecientes;

    private final EstrellasPuntuacion stars;
    private final Span puntuacion;
    private final Span cantidadOpiniones;

    private final VerticalLayout caracteristicasLayout;

    private final Button gestionarOpinionesButton;
    private final Button verListaOpinionesButton;

    private final VisualizadorOpinion opinion1;
    private final VisualizadorOpinion opinion2;

    public ListaOpinionesView(SecurityService securityService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, OpinionService opinionService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.opinionService = opinionService;

        stars = new EstrellasPuntuacion();
        puntuacion = new Span();
        cantidadOpiniones = new Span();
        caracteristicasLayout = new VerticalLayout();
        gestionarOpinionesButton = new Button(getTranslation("views.opiniones.gestionaropinion"));
        verListaOpinionesButton = new Button(getTranslation("views.opiniones.verlistaopiniones"));
        opinion1 = new VisualizadorOpinion();
        opinion2 = new VisualizadorOpinion();

        configurarUI();
        cargarDatos();
    }

    private void configurarUI() {
        puntuacion.setClassName("puntuacion-label");
        stars.addClassNames(LumoUtility.Padding.Horizontal.XSMALL);
        cantidadOpiniones.addClassNames(LumoUtility.Padding.Horizontal.SMALL);

        VerticalLayout puntuacionLayout = new VerticalLayout();
        puntuacionLayout.setSpacing(false);
        puntuacionLayout.setWidth("unset");
        puntuacionLayout.addClassNames(LumoUtility.Gap.SMALL);
        puntuacionLayout.setAlignSelf(Alignment.CENTER, puntuacion);
        puntuacionLayout.add(puntuacion, stars, cantidadOpiniones);

        caracteristicasLayout.getThemeList().clear();
        caracteristicasLayout.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Bottom.MEDIUM);

        gestionarOpinionesButton.addClassNames(LumoUtility.Margin.MEDIUM);

        VerticalLayout primeraColumnaLayout = new VerticalLayout();
        primeraColumnaLayout.setSpacing(false);
        primeraColumnaLayout.setWidth("300px");
        primeraColumnaLayout.add(puntuacionLayout, caracteristicasLayout, gestionarOpinionesButton);

        VerticalLayout opinionesLayout = new VerticalLayout();
        opinionesLayout.setHeightFull();
        opinionesLayout.setWidth("300px");
        opinionesLayout.setFlexGrow(1.0);

        opinion1.setWidthFull();
        opinion1.addClassName(LumoUtility.Padding.MEDIUM);
        opinion2.setWidthFull();
        opinion2.addClassName(LumoUtility.Padding.MEDIUM);
        verListaOpinionesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        verListaOpinionesButton.addClassName(LumoUtility.Margin.MEDIUM);
        verListaOpinionesButton.addClickListener(ce -> {
            List<Opinion> opiniones = opinionService.getAllOpinionesByRestaurante(restaurante.getId());
            OpinionesDialog opinionesDialog = new OpinionesDialog(opiniones);
            opinionesDialog.open();
        });

        opinionesLayout.addClassNames(LumoUtility.Padding.MEDIUM);
        opinionesLayout.add(opinion1, opinion2, verListaOpinionesButton);

        add(primeraColumnaLayout, opinionesLayout);
        this.setWidth("80%");
        this.setMinWidth("600px");
        this.getStyle().setMargin("auto");
        this.setFlexGrow(1, opinionesLayout);
    }

    private void cargarDatos() {
        restaurante = restauranteService
                .getByCorreo(securityService.getAuthenticatedUser().orElseThrow().getUsername())
                .orElseThrow();
        try {
            detalleRestaurante = detalleRestauranteService.get(restaurante.getIdDetalleRestaurante());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        opinionesRecientes = opinionService.getOpinionesRecientesByRestaurante(restaurante.getId());

        puntuacion.add(String.valueOf(restaurante.getPuntuacion()));
        stars.setValue(restaurante.getPuntuacion());
        cantidadOpiniones.add(getTranslation("views.opiniones.cantidadopiniones", restaurante.getCantidadOpiniones()));

        for (Map.Entry<String, CalificacionPromedio> entry : detalleRestaurante.getCaracteristicas().entrySet()) {
            EstrellasPuntuacion estrellasPuntuacion = new EstrellasPuntuacion();
            estrellasPuntuacion.setLabel(entry.getKey());
            estrellasPuntuacion.setValue(entry.getValue().getPuntuacion());
            estrellasPuntuacion.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM);
            caracteristicasLayout.add(estrellasPuntuacion);
        }

        if (detalleRestaurante.getCaracteristicas().isEmpty()) {
            caracteristicasLayout.setVisible(false);
        }

        if (opinionesRecientes.isEmpty()) {
            opinion1.setVisible(false);
            opinion2.setVisible(false);
        } else if (opinionesRecientes.size() == 1) {
            opinion1.setValue(opinionesRecientes.get(0));
            opinion2.setVisible(false);
        } else {
            opinion1.setValue(opinionesRecientes.get(0));
            opinion2.setValue(opinionesRecientes.get(1));
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
