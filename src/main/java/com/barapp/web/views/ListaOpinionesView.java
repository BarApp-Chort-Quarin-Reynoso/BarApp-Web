package com.barapp.web.views;

import com.barapp.web.business.service.*;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.views.components.VisualizadorOpinion;
import com.barapp.web.views.components.charts.BarappCharts;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.Divider;
import com.barapp.web.views.components.pageElements.MainElement;
import com.barapp.web.views.components.puntuacion.EstrellasPuntuacion;
import com.barapp.web.views.dialogs.EditorCaracteristicasOpinionDialog;
import com.barapp.web.views.dialogs.OpinionesDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@PageTitle("Mis estadísticas")
@Route(value = "mis-opiniones", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class ListaOpinionesView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final DetalleRestauranteService detalleRestauranteService;
    private final OpinionService opinionService;
    private final ConfiguracionService configuracionService;
    private final EstadisticaService estadisticaService;

    private Restaurante restaurante;
    private DetalleRestaurante detalleRestaurante;
    private List<Opinion> opinionesRecientes;
    private Estadistica estadistica;

    private final EstrellasPuntuacion stars;
    private final Span puntuacion;
    private final Span cantidadOpiniones;

    private final VerticalLayout caracteristicasLayout;

    private final Button gestionarCaracteristicasButton;
    private final Button verListaOpinionesButton;

    private final VerticalLayout opinionesLayout;

    private final Span noHayOpiniones;
    private final Span diasActivo;
    private final Span reservasConcretadas;
    private final Span clientesAtendidos;

    private final Div chartOcupacionxDiaContainer = new Div();
    private final Div chartOcupacionxTipoComidaContainer = new Div();

    public ListaOpinionesView(SecurityService securityService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, OpinionService opinionService, ConfiguracionService configuracionService, EstadisticaService estadisticaService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.opinionService = opinionService;
        this.configuracionService = configuracionService;
        this.estadisticaService = estadisticaService;

        stars = new EstrellasPuntuacion();
        puntuacion = new Span();
        cantidadOpiniones = new Span();
        caracteristicasLayout = new VerticalLayout();
        gestionarCaracteristicasButton = new Button(getTranslation("views.opiniones.gestionarcaracteristicas"));
        verListaOpinionesButton = new Button(getTranslation("views.opiniones.verlistaopiniones"));
        opinionesLayout = new VerticalLayout();
        noHayOpiniones = new Span(getTranslation("views.opiniones.nohayopiniones"));
        reservasConcretadas = new Span();
        diasActivo = new Span();
        clientesAtendidos = new Span();

        configurarUI();
        cargarDatos();
    }

    private void configurarUI() {
        MainElement mainElement = new MainElement(getOpinionesSection(), new Divider(), getEstadisticasSection());
        mainElement.addClassName("mis-opiniones-view");

        this.add(mainElement, new BarappFooter());
        this.setPadding(false);
        this.setSpacing(false);
        this.setSizeFull();
    }

    private VerticalLayout getOpinionesSection() {
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
        primeraColumnaLayout.getThemeList().clear();
        primeraColumnaLayout.setWidth("300px");
        primeraColumnaLayout.add(puntuacionLayout, caracteristicasLayout, gestionarCaracteristicasButton);

        opinionesLayout.getThemeList().clear();
        opinionesLayout.setHeightFull();
        opinionesLayout.setWidth("300px");
        opinionesLayout.setFlexGrow(1.0);
        opinionesLayout.addClassNames(LumoUtility.Padding.MEDIUM, "opiniones-opinionesLayout");

        verListaOpinionesButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        verListaOpinionesButton.addClassName(LumoUtility.Margin.MEDIUM);
        verListaOpinionesButton.addClickListener(ce -> {
            List<Opinion> opiniones = opinionService.getAllOpinionesByRestaurante(restaurante.getId());
            OpinionesDialog opinionesDialog = new OpinionesDialog(opiniones);
            opinionesDialog.open();
        });

        HorizontalLayout columnsWrapper = new HorizontalLayout(primeraColumnaLayout, opinionesLayout);
        columnsWrapper.setWidthFull();

        VerticalLayout section = new VerticalLayout();
        section.add(new H2(getTranslation("views.opiniones.opiniones.title")), columnsWrapper);
        return section;
    }

    private VerticalLayout getEstadisticasSection() {
        reservasConcretadas.setClassName("estadisticas-dato");
        H4 reservasConcretadasLabel = new H4(getTranslation("views.opiniones.reservasconcretadas.label"));
        reservasConcretadasLabel.setClassName("estadisticas-titulo");

        diasActivo.setClassName("estadisticas-dato");
        H4 diasActivoLabel = new H4(getTranslation("views.opiniones.diasactivo.label"));
        diasActivoLabel.setClassName("estadisticas-titulo");

        clientesAtendidos.setClassName("estadisticas-dato");
        H4 clientesAtendidosLabel = new H4(getTranslation("views.opiniones.clientesatendidos.label"));
        clientesAtendidosLabel.setClassName("estadisticas-titulo");

        VerticalLayout reservasConcretadasLayout = new VerticalLayout(reservasConcretadas, reservasConcretadasLabel);
        reservasConcretadasLayout.setSpacing(false);
        reservasConcretadasLayout.setAlignItems(Alignment.CENTER);
        reservasConcretadasLayout.setWidth("unset");

        VerticalLayout diasActivoLayout = new VerticalLayout(diasActivo, diasActivoLabel);
        diasActivoLayout.setSpacing(false);
        diasActivoLayout.setAlignItems(Alignment.CENTER);
        diasActivoLayout.setWidth("unset");

        VerticalLayout clientesAtendidosLayout = new VerticalLayout(clientesAtendidos, clientesAtendidosLabel);
        clientesAtendidosLayout.setSpacing(false);
        clientesAtendidosLayout.setAlignItems(Alignment.CENTER);
        clientesAtendidosLayout.setWidth("unset");

        HorizontalLayout datosLayout = new HorizontalLayout(
                reservasConcretadasLayout, diasActivoLayout, clientesAtendidosLayout);
        datosLayout.setWidthFull();
        datosLayout.setJustifyContentMode(JustifyContentMode.EVENLY);

        HorizontalLayout chartsInnerLayout = new HorizontalLayout(
                chartOcupacionxTipoComidaContainer, chartOcupacionxDiaContainer);

        VerticalLayout chartsLayout = new VerticalLayout();
        chartsLayout.add(new H3(getTranslation("views.opiniones.ocupacion.title")), chartsInnerLayout);

        VerticalLayout estadisticasLayout = new VerticalLayout();
        estadisticasLayout.add(datosLayout, chartsLayout);
        estadisticasLayout.setSpacing(false);
        estadisticasLayout.addClassNames(LumoUtility.Gap.LARGE);

        VerticalLayout statisticsSection = new VerticalLayout();
        statisticsSection.add(new H2(getTranslation("views.opiniones.estadisticas")));
        statisticsSection.add(estadisticasLayout);
        return statisticsSection;
    }

    private void cargarDatos() {
        restaurante = restauranteService
                .getByCorreo(securityService.getAuthenticatedUser().orElseThrow().getUsername())
                .orElseThrow();
        try {
            detalleRestaurante = detalleRestauranteService.get(restaurante.getIdDetalleRestaurante());
            estadistica = estadisticaService.getByCorreoRestaurante(restaurante.getCorreo()).orElseThrow();
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

        if (restaurante.getCantidadOpiniones() == 0) {
            noHayOpiniones.addClassNames("empty-label", LumoUtility.Padding.MEDIUM);
            opinionesLayout.add(noHayOpiniones);
        } else {
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

        reservasConcretadas.add(String.valueOf(estadistica.getReservasConcretadas()));
        diasActivo.add(String.valueOf(estadistica.getDiasActivo()));
        clientesAtendidos.add(String.valueOf(estadistica.getClientesAtendidos()));
        chartOcupacionxTipoComidaContainer.add(BarappCharts.getBarChart(
                Arrays
                        .stream(TipoComida.values())
                        .map(tipo -> getTranslation(tipo.getTranslationKey()))
                        .toArray(String[]::new),
                estadistica.getPorcentajeOcupacionxTipoComida().entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .map(Map.Entry::getValue).toArray(Double[]::new),
                getTranslation("views.opiniones.ocupacion.ocupacionxtipocomida")
        ));
        chartOcupacionxDiaContainer.add(BarappCharts.getBarChart(
                FormatUtils.getWeekdays().toArray(String[]::new),
                estadistica.getPorcentajeOcupacionxDiaSemana().entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .map(Map.Entry::getValue).toArray(Double[]::new),
                getTranslation("views.opiniones.ocupacion.ocupacionxdiasemana")
        ));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }
}
