package com.barapp.web.views;

import com.barapp.web.business.service.ConfiguracionService;
import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.business.service.OpinionService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.views.components.VisualizadorOpinion;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.MainElement;
import com.barapp.web.views.components.puntuacion.EstrellasPuntuacion;
import com.barapp.web.views.dialogs.EditorCaracteristicasOpinionDialog;
import com.barapp.web.views.dialogs.OpinionesDialog;
import com.vaadin.flow.component.UI;
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
public class ListaOpinionesView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final DetalleRestauranteService detalleRestauranteService;
    private final OpinionService opinionService;
    private final ConfiguracionService configuracionService;

    private Restaurante restaurante;
    private DetalleRestaurante detalleRestaurante;
    private List<Opinion> opinionesRecientes;

    private final EstrellasPuntuacion stars;
    private final Span puntuacion;
    private final Span cantidadOpiniones;

    private final VerticalLayout caracteristicasLayout;

    private final Button gestionarCaracteristicasButton;
    private final Button verListaOpinionesButton;

    private final VerticalLayout opinionesLayout;

    public ListaOpinionesView(SecurityService securityService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, OpinionService opinionService, ConfiguracionService configuracionService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.opinionService = opinionService;
        this.configuracionService = configuracionService;

        stars = new EstrellasPuntuacion();
        puntuacion = new Span();
        cantidadOpiniones = new Span();
        caracteristicasLayout = new VerticalLayout();
        gestionarCaracteristicasButton = new Button(getTranslation("views.opiniones.gestionarcaracteristicas"));
        verListaOpinionesButton = new Button(getTranslation("views.opiniones.verlistaopiniones"));
        opinionesLayout = new VerticalLayout();

        configurarUI();
        cargarDatos();
    }

    private void configurarUI() {
        puntuacion.setClassName("puntuacion-label");
        stars.addClassNames(LumoUtility.Padding.Horizontal.XSMALL);
        cantidadOpiniones.addClassNames(LumoUtility.Padding.Horizontal.SMALL);
        cantidadOpiniones.getStyle().set("font-style", "italic");

        VerticalLayout puntuacionLayout = new VerticalLayout();
        puntuacionLayout.setSpacing(false);
        puntuacionLayout.setWidth("unset");
        puntuacionLayout.addClassNames(LumoUtility.Gap.SMALL);
        puntuacionLayout.setAlignSelf(Alignment.CENTER, puntuacion);
        puntuacionLayout.add(puntuacion, stars, cantidadOpiniones);

        caracteristicasLayout.getThemeList().clear();
        caracteristicasLayout.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Bottom.MEDIUM);

        gestionarCaracteristicasButton.addClassNames(LumoUtility.Margin.MEDIUM);
        gestionarCaracteristicasButton.addClickListener(ce -> {
            Configuracion configuracion = configuracionService.getRestaurantesConfig();
            EditorCaracteristicasOpinionDialog dialog = new EditorCaracteristicasOpinionDialog(
                    detalleRestaurante, configuracion.getCaracteristicas());
            dialog.addSaveListener(se -> {
                try {
                    detalleRestauranteService.actualizarCaracteristicas(detalleRestaurante.getId(), se.getBean());
                    UI.getCurrent().getPage().reload();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            dialog.open();
        });

        VerticalLayout primeraColumnaLayout = new VerticalLayout();
        primeraColumnaLayout.setSpacing(false);
        primeraColumnaLayout.setWidth("300px");
        primeraColumnaLayout.add(puntuacionLayout, caracteristicasLayout, gestionarCaracteristicasButton);

        opinionesLayout.setHeightFull();
        opinionesLayout.setWidth("300px");
        opinionesLayout.setFlexGrow(1.0);

        verListaOpinionesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        verListaOpinionesButton.addClassName(LumoUtility.Margin.MEDIUM);
        verListaOpinionesButton.addClickListener(ce -> {
            List<Opinion> opiniones = opinionService.getAllOpinionesByRestaurante(restaurante.getId());
            OpinionesDialog opinionesDialog = new OpinionesDialog(opiniones);
            opinionesDialog.open();
        });

        opinionesLayout.addClassNames(LumoUtility.Padding.MEDIUM, "opiniones-opinionesLayout");

        MainElement mainElement = new MainElement(new HorizontalLayout(primeraColumnaLayout, opinionesLayout));
        mainElement.addClassName("mis-opiniones-view");

        this.add(mainElement, new BarappFooter());
        this.setPadding(false);
        this.setSpacing(false);
        this.setSizeFull();
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

        puntuacion.add(FormatUtils.puntuacionFormat().format(restaurante.getPuntuacion()));
        stars.setValue(restaurante.getPuntuacion());
        cantidadOpiniones.add(restaurante.getCantidadOpiniones() != 1
                ? getTranslation("views.opiniones.cantidadopiniones.opiniones", restaurante.getCantidadOpiniones())
                : getTranslation("views.opiniones.cantidadopiniones.opinion"));

        for (Map.Entry<String, CalificacionPromedio> entry : detalleRestaurante.getCaracteristicas().entrySet()) {
            EstrellasPuntuacion estrellasPuntuacion = new EstrellasPuntuacion();
            estrellasPuntuacion.setLabel(entry.getKey());
            estrellasPuntuacion.setValue(entry.getValue().getPuntuacion());
            estrellasPuntuacion.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM);
            if (entry.getValue().getCantidadOpiniones() == 0) {
                estrellasPuntuacion.setSinOpiniones(true);
            }
            caracteristicasLayout.add(estrellasPuntuacion);
        }

        if (detalleRestaurante.getCaracteristicas().isEmpty()) {
            caracteristicasLayout.setVisible(false);
        }

        for (int i = 0; i < 3; i++) {
            if (i < opinionesRecientes.size()) {
                VisualizadorOpinion opinion = new VisualizadorOpinion();
                opinion.setWidthFull();
                opinion.addClassName(LumoUtility.Padding.MEDIUM);
                opinion.setValue(opinionesRecientes.get(i));
                opinionesLayout.add(opinion);
            }
        }

        if (restaurante.getCantidadOpiniones() > 3) {
            opinionesLayout.add(verListaOpinionesButton);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
