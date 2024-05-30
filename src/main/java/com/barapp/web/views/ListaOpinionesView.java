package com.barapp.web.views;

import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.business.service.OpinionService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.model.Opinion;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.views.components.EstrellasPuntuacion;
import com.barapp.web.views.components.VisualizadorOpinion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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

    private final Button botonGestionarOpinion;

    private final VirtualList<Opinion> virtualListOpiniones;

    public ListaOpinionesView(SecurityService securityService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, OpinionService opinionService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.opinionService = opinionService;

        stars = new EstrellasPuntuacion();
        puntuacion = new Span();
        cantidadOpiniones = new Span();
        caracteristicasLayout = new VerticalLayout();
        botonGestionarOpinion = new Button(getTranslation("views.opiniones.gestionaropinion"));
        virtualListOpiniones = new VirtualList<>();

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

        botonGestionarOpinion.addClassNames(LumoUtility.Margin.MEDIUM);

        VerticalLayout primeraColumnaLayout = new VerticalLayout();
        primeraColumnaLayout.setSpacing(false);
        primeraColumnaLayout.setWidth("300px");
        primeraColumnaLayout.add(puntuacionLayout, caracteristicasLayout, botonGestionarOpinion);

        virtualListOpiniones.setWidth("400px");
        virtualListOpiniones.setHeight("400px");
        virtualListOpiniones.setRenderer(new ComponentRenderer<>(opinion -> {
            VisualizadorOpinion visualizadorOpinion = new VisualizadorOpinion(opinion);
            visualizadorOpinion.setWidthFull();
            visualizadorOpinion.addClassNames(LumoUtility.Padding.MEDIUM);
            return visualizadorOpinion;
        }));
        virtualListOpiniones.addClassNames(LumoUtility.Padding.MEDIUM);

        add(primeraColumnaLayout, virtualListOpiniones);
        this.setWidth("70%");
        this.setMinWidth("800px");
        this.getStyle().setMargin("auto");
        this.setFlexGrow(1, virtualListOpiniones);
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

        for (Map.Entry<String, Double> entry : detalleRestaurante.getCaracteristicas().entrySet()) {
            EstrellasPuntuacion estrellasPuntuacion = new EstrellasPuntuacion();
            estrellasPuntuacion.setLabel(entry.getKey());
            estrellasPuntuacion.setValue(entry.getValue());
            estrellasPuntuacion.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM);
            caracteristicasLayout.add(estrellasPuntuacion);
        }

        if (detalleRestaurante.getCaracteristicas().isEmpty()) {
            caracteristicasLayout.setVisible(false);
        }

        virtualListOpiniones.setItems(opinionesRecientes);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
