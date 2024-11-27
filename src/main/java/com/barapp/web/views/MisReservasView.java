package com.barapp.web.views;

import com.barapp.web.business.service.ReservaService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.http.request.RestauranteInfoQR;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.enums.EstadoReserva;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.QRCodeGenerator;
import com.barapp.web.utils.UiUtils;
import com.barapp.web.views.components.ReservasPausadasCard;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.MainElement;
import com.flowingcode.vaadin.addons.badgelist.Badge;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.partitioningBy;

@PageTitle("Mis Reservas")
@Route(value = "mis-reservas", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class MisReservasView extends VerticalLayout implements BeforeEnterObserver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final ReservaService reservaService;

    private Restaurante restaurante;

    List<Reserva> reservasPendientesList;
    List<Reserva> reservasPasadasList;
    Grid<Reserva> pendientesGrid = new Grid<>();
    Grid<Reserva> pasadasGrid = new Grid<>();
    VerticalLayout pendientesEmptyState;
    VerticalLayout pasadasEmptyState;
    Component pendientesComponent;
    Component pasadasComponent;
    TextArea motivo = new TextArea(
            getTranslation("view.misreservas.motivocancelacion"),
            getTranslation("view.misreservas.ejemplocancelacion")
    );

    ReservasPausadasCard reservasPausadasContainer;
    HorizontalLayout reservasActivadasContainer;

    public MisReservasView(SecurityService securityService, RestauranteService restauranteService, ReservaService reservaService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.reservaService = reservaService;
        this.addClassName("mis-reservas-view");

        UserDetails userDetails = this.securityService
                .getAuthenticatedUser()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Optional<Restaurante> optRestaurante = this.restauranteService.getByCorreo(userDetails.getUsername());
        restaurante = optRestaurante.orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        List<Reserva> reservas = this.reservaService.getReservasByRestaurante(restaurante.getId());
        Map<Boolean, List<Reserva>> pendientesPasadas = reservas.stream()
                .collect(partitioningBy(reserva -> reserva.getEstado().equals(EstadoReserva.PENDIENTE)));
        reservasPendientesList = pendientesPasadas.get(true); // Obtener reservas pendientes del map
        reservasPasadasList = pendientesPasadas.get(false); // Obtener eservas pasadas del map

        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        configurarElementos();
    }

    private void configurarElementos() {
        H2 titulo = new H2(getTranslation("view.misreservas.gestionareservas"));
        Button generarQr = new Button(getTranslation("view.misreservas.generarQR"), new Icon(VaadinIcon.QRCODE));
        generarQr.addClickListener(event -> {
            generarCodigoQR();
        });
        generarQr.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        HorizontalLayout headerLayout = new HorizontalLayout(titulo, generarQr);
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        TabSheet tabSheet = configurarTabsheet();

        HorizontalLayout pausaContainer = configurarPausa();

        MainElement mainElement = new MainElement(headerLayout, tabSheet, pausaContainer);
        mainElement.addClassName("mis-reservas-card");
        add(mainElement, new BarappFooter());
    }

    private TabSheet configurarTabsheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        pendientesEmptyState = getEmptyState(getTranslation("view.misreservas.pendientes"));
        pendientesComponent = getGridPendiente();

        pasadasEmptyState = getEmptyState(getTranslation("view.misreservas.pasadas"));
        pasadasComponent = getGridPasadas();

        updateGrids();

        VerticalLayout pendientesContainer = new VerticalLayout(pendientesEmptyState, pendientesComponent);
        VerticalLayout pasadasContainer = new VerticalLayout(pasadasEmptyState, pasadasComponent);
        pendientesContainer.setSizeFull();
        pendientesContainer.setPadding(false);
        pasadasContainer.setSizeFull();
        pasadasContainer.setPadding(false);

        tabSheet.add(getTranslation("view.misreservas.pendientes"), pendientesContainer);
        tabSheet.add(getTranslation("view.misreservas.pasadas"), pasadasContainer);
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED);

        return tabSheet;
    }

    private HorizontalLayout configurarPausa() {
        reservasPausadasContainer = new ReservasPausadasCard();
        reservasPausadasContainer.addReservasReanudadasListener(event -> {
            setPausado(false);
        });
        reservasActivadasContainer = getReservasActivadasContainer();
        HorizontalLayout pausaContainer = new HorizontalLayout(reservasPausadasContainer, reservasActivadasContainer);
        pausaContainer.setPadding(false);

        boolean estaPausado = restaurante.getEstado().equals(EstadoRestaurante.PAUSADO);

        reservasPausadasContainer.setVisible(estaPausado);
        reservasActivadasContainer.setVisible(!estaPausado);

        return pausaContainer;
    }

    private Component getGridPendiente() {
        pendientesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        Grid.Column<Reserva> nombreUsuarioColumn = pendientesGrid.addColumn(reserva -> reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido()).setSortable(true);
        Grid.Column<Reserva> diaColumn = pendientesGrid.addColumn(new LocalDateRenderer<>(Reserva::getFecha,"dd MMM.")).setSortable(true).setComparator(Reserva::getFecha);
        Grid.Column<Reserva> horaColumn = pendientesGrid.addColumn(new LocalDateTimeRenderer<>(reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario()),"kk:mm")).setSortable(true).setComparator(Comparator.comparing(reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario())));
        Grid.Column<Reserva> cantidadPersonasColumn = pendientesGrid.addColumn(Reserva::getCantidadPersonas).setSortable(true);

        GridListDataView<Reserva> dataView = pendientesGrid.setItems(reservasPendientesList);
        ReservaFilter reservaFilter = new ReservaFilter(dataView);

        pendientesGrid.getHeaderRows().clear();
        HeaderRow headerRow = pendientesGrid.appendHeaderRow();

        headerRow
                .getCell(nombreUsuarioColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.nombreusuario"),
                        reservaFilter::setNombre
                ));
        headerRow
                .getCell(diaColumn)
                .setComponent(
                        UiUtils.createFilterHeader(getTranslation("view.misreservas.dia"), reservaFilter::setDia));
        headerRow
                .getCell(horaColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.horario"),
                        reservaFilter::setHorario
                ));
        headerRow
                .getCell(cantidadPersonasColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.cantidadpersonas"),
                        reservaFilter::setCantidadPersonas
                ));
        pendientesGrid.addColumn(new ComponentRenderer<>(MenuBar::new, (menu, reserva) -> {
                    menu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
                    MenuItem qr = menu.addItem(new Icon(VaadinIcon.CHECK_CIRCLE_O), e -> {
                        concretarReservaManualmente(reserva);
                    });
                    qr.getElement().setAttribute("title", getTranslation("view.misreservas.concretarreserva"));

                    MenuItem noCumplida = menu.addItem(new Icon(VaadinIcon.USER_CLOCK), e -> {
                        marcarComoNoCumplida(reserva);
                    });
                    noCumplida.getElement().setAttribute("title", getTranslation("view.misreservas.marcarnocumplida"));
                    MenuItem cancelar = menu.addItem(new Icon(VaadinIcon.CLOSE_CIRCLE_O), e -> {
                        cancelarReserva(reserva);
                    });
                    cancelar.getElement().setAttribute("title", getTranslation("view.misreservas.cancelarreserva"));
                    cancelar.addClassNames(LumoUtility.Background.ERROR_10);
                })
        ).setHeader(getTranslation("view.misreservas.acciones"));

        pendientesGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        pendientesGrid.sort(List.of(
                new GridSortOrder<>(diaColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(horaColumn, SortDirection.ASCENDING),
                new GridSortOrder<>(cantidadPersonasColumn, SortDirection.DESCENDING)
        ));
        pendientesGrid.setPartNameGenerator(reserva -> {
            if (reserva.getFecha().isEqual(LocalDate.now()))
                return "es-hoy";
            return null;
        });

        return pendientesGrid;
    }

    private Component getGridPasadas() {
        pasadasGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        Grid.Column<Reserva> nombreUsuarioColumn = pasadasGrid
                .addColumn(reserva -> reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido())
                .setSortable(true);
        Grid.Column<Reserva> diaColumn = pasadasGrid
                .addColumn(new LocalDateRenderer<>(Reserva::getFecha, "dd/MM"))
                .setSortable(true)
                .setComparator(Reserva::getFecha);
        Grid.Column<Reserva> horaColumn = pasadasGrid
                .addColumn(new LocalDateTimeRenderer<>(
                        reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario()), "kk:mm"))
                .setSortable(true)
                .setComparator(Comparator.comparing(
                        reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario())));
        Grid.Column<Reserva> cantidadPersonasColumn = pasadasGrid
                .addColumn(Reserva::getCantidadPersonas)
                .setSortable(true);

        GridListDataView<Reserva> dataView = pasadasGrid.setItems(reservasPasadasList);
        ReservaFilter reservaFilter = new ReservaFilter(dataView);

        pasadasGrid.getHeaderRows().clear();
        HeaderRow headerRow = pasadasGrid.appendHeaderRow();

        headerRow
                .getCell(nombreUsuarioColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.nombreusuario"),
                        reservaFilter::setNombre
                ));
        headerRow
                .getCell(diaColumn)
                .setComponent(
                        UiUtils.createFilterHeader(getTranslation("view.misreservas.dia"), reservaFilter::setDia));
        headerRow
                .getCell(horaColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.horario"),
                        reservaFilter::setHorario
                ));
        headerRow
                .getCell(cantidadPersonasColumn)
                .setComponent(UiUtils.createFilterHeader(getTranslation("view.misreservas.cantidadpersonas"),
                        reservaFilter::setCantidadPersonas
                ));
        pasadasGrid.addColumn(new ComponentRenderer<>(Badge::new, (badge, reserva) -> {
            EstadoReserva estado = reserva.getEstado();
            badge.setText(getTranslation(estado.getTranslationKey()));
            switch (estado) {
                case CONCRETADA:
                    badge.addThemeName("success");
                    break;
                case NO_ASISTIO:
                    badge.addThemeName("error");
                    break;
                case CANCELADA_USUARIO:
                case CANCELADA_BAR:
                    break;
            }
        })).setHeader(getTranslation("view.misreservas.estados"));

        pasadasGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        pasadasGrid.sort(List.of(
                new GridSortOrder<>(diaColumn, SortDirection.DESCENDING),
                new GridSortOrder<>(horaColumn, SortDirection.DESCENDING)
        ));

        return pasadasGrid;
    }

    private void generarCodigoQR() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle(getTranslation("view.misreservas.generarQR"));

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setClassName("concretar-content");

        RestauranteInfoQR restauranteInfoQR = new RestauranteInfoQR(restaurante.getId());
        Gson gson = new Gson();
        String restauranteQR = gson.toJson(restauranteInfoQR);

        Paragraph texto1 = new Paragraph(getTranslation("view.misreservas.pedilequeescanee"));

        dialogLayout.add(texto1);

        try {
            Button cerrarButton = new Button(getTranslation("commons.cerrar"), e -> dialog.close());
            Button imprimirQR = new Button(getTranslation("view.misreservas.descargarQR"));
            imprimirQR.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            imprimirQR.addClickListener(event -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                dialog.close();
            });

            // Generate and Return Qr Code in Byte Array
            byte[] image = QRCodeGenerator.getQRCodeImage(restauranteQR, 300, 300);
            Anchor downloadLink = new Anchor(new StreamResource(
                    restaurante.getNombre() + "QR.jpg",
                    () -> new ByteArrayInputStream(image)
            ), restaurante.getNombre() + "QR.jpg");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.removeAll();
            downloadLink.add(imprimirQR);

            Image qr = new Image(new StreamResource("", () -> new ByteArrayInputStream(image)), "");
            dialogLayout.add(qr);

            dialog.add(dialogLayout);

            dialog.getFooter().add(cerrarButton, downloadLink);
        } catch (WriterException | IOException e) {
            logger.error("Error al generar el código QR", e);
        }

        dialog.open();
    }

    private void concretarReservaManualmente(Reserva reserva) {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle(getTranslation("view.misreservas.concretarreserva"));

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setClassName("concretar-content");

        Paragraph texto = new Paragraph(getTranslation("view.misreservas.sinopuedeescanear"));
        Paragraph texto2 = new Paragraph(getTranslation("view.misreservas.tiempoparaconfirmar"));

        dialogLayout.add(texto, texto2);

        dialog.add(dialogLayout);

        Button cerrarButton = new Button(getTranslation("commons.cerrar"), e -> dialog.close());
        Button concretarManualmenteButton = new Button(getTranslation("view.misreservas.concretarmanualmente"));
        concretarManualmenteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        concretarManualmenteButton.addClickListener(event -> {
            Reserva reservaActualizada = this.reservaService.concretarReserva(reserva.getId(), reserva.getUsuario().getId(), reserva.getRestaurante().getId());
            this.reservasPendientesList.remove(reserva);
            this.reservasPasadasList.add(reservaActualizada);
            this.updateGrids();
            dialog.close();
        });
        boolean sePuedeConcretar = LocalDateTime
                .now()
                .isAfter(LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario()).minusMinutes(30));
        concretarManualmenteButton.setEnabled(sePuedeConcretar);

        dialog.getFooter().add(cerrarButton, concretarManualmenteButton);

        dialog.open();
    }

    private void marcarComoNoCumplida(Reserva reserva) {
        Dialog dialog = new Dialog();

        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.setClassName("marcar-no-cumplida-content");

        LocalDateTime fechaReserva = LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario());
        LocalDateTime fechaMaxima = fechaReserva.plusMinutes(30);
        LocalDateTime fechaActual = LocalDateTime.now();

        String headerTitle;
        Icon icon;
        String iconColor;
        Paragraph paragraph;
        String textoBotonSecundario;
        String textoBotonPrimario = "";

        if (fechaActual.isAfter(fechaMaxima)) {
            headerTitle = getTranslation("view.misreservas.clientenosepresento");
            icon = VaadinIcon.WARNING.create();
            iconColor = "#fd2c2c";
            paragraph = new Paragraph(getTranslation("view.misreservas.hanpasado30min"));
            textoBotonSecundario = getTranslation("view.misreservas.esperarmastiempo");
            textoBotonPrimario = getTranslation("view.misreservas.marcarnocumplida");
        } else if (fechaActual.isAfter(fechaReserva) && fechaActual.isBefore(fechaMaxima)) {
            String minutos;
            int minutosRestantes = (int) fechaActual.until(fechaMaxima, ChronoUnit.MINUTES);
            minutos = switch (minutosRestantes) {
                case 1 -> getTranslation("view.misreservas.1minuto");
                case 0 -> getTranslation("view.misreservas.menosde1minuto");
                default -> minutosRestantes + " " + getTranslation("view.misreservas.minutos");
            };

            headerTitle = getTranslation("view.misreservas.clienteretrasado");
            icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
            iconColor = "#ff8300e3";
            paragraph = new Paragraph(getTranslation("view.misreservas.aunnopasaron30", minutos));
            textoBotonSecundario = getTranslation("view.misreservas.esperarmastiempo");
        } else {
            headerTitle = getTranslation("view.misreservas.marcarnocumplida");
            icon = VaadinIcon.INFO_CIRCLE_O.create();
            iconColor = "#079ee8";
            paragraph = new Paragraph(getTranslation("view.misreservas.aquipodrasmarcar"));
            textoBotonSecundario = getTranslation("commons.aceptar");
        }

        dialog.setHeaderTitle(headerTitle);
        icon.setClassName("dialog-icon");
        icon.setColor(iconColor);
        dialogLayout.add(icon, paragraph);
        dialog.add(dialogLayout);

        Button botonSecundario = new Button(textoBotonSecundario, e -> dialog.close());

        if (!textoBotonPrimario.isEmpty()) {
            Button botonPrimario = new Button(textoBotonPrimario);
            botonPrimario.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            botonPrimario.addClickListener(event -> {
                Reserva reservaActualizada = this.reservaService.updateEstado(
                        reserva.getId(), EstadoReserva.NO_ASISTIO.toString());
                this.reservasPendientesList.remove(reserva);
                this.reservasPasadasList.add(reservaActualizada);
                this.updateGrids();
                dialog.close();
            });

            dialog.getFooter().add(botonSecundario, botonPrimario);
        } else {
            dialog.getFooter().add(botonSecundario);
        }

        dialog.open();
    }

    private void cancelarReserva(Reserva reserva) {
        Dialog dialog = new Dialog();

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setClassName("cancelar-reserva-content");

        LocalDateTime fechaReserva = LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario());
        LocalDateTime fechaMaxima = fechaReserva.minusHours(1);
        LocalDateTime fechaActual = LocalDateTime.now();

        String headerTitle;
        Paragraph paragraph;
        motivo.setMaxHeight("225px");
        motivo.setWidthFull();
        motivo.setHeight("150px");
        motivo.setMaxLength(500);
        motivo.setHelperText("0/500");
        motivo.setValueChangeMode(ValueChangeMode.EAGER);
        motivo.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 500);
        });

        if (fechaActual.isBefore(fechaMaxima)) { // Se puede cancelar
            headerTitle = getTranslation("view.misreservas.confirmarcancelacion");
            paragraph = new Paragraph(getTranslation("view.misreservas.deseascancelar"));
        } else { // No se puede cancelar
            headerTitle = getTranslation("view.misreservas.nosepuedecancelar");
            paragraph = new Paragraph(getTranslation("view.misreservas.programadaparacomenzar"));
        }

        dialog.setHeaderTitle(headerTitle);

        Button cerrarButton = new Button(getTranslation("commons.cerrar"), e -> dialog.close());

        if (fechaActual.isBefore(fechaMaxima)) {
            dialogLayout.add(paragraph, motivo);

            Button cancelarButton = new Button(getTranslation("view.misreservas.cancelarreserva"));
            cancelarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            cancelarButton.addClickListener(event -> {
                // TODO: Enviar notificación al cliente
                Reserva reservaActualizada = this.reservaService.cancelarReserva(
                        reserva.getId(), EstadoReserva.CANCELADA_BAR.toString(), motivo.getValue());
                this.reservasPendientesList.remove(reserva);
                this.reservasPasadasList.add(reservaActualizada);
                this.updateGrids();
                dialog.close();
            });
            dialog.getFooter().add(cerrarButton, cancelarButton);
        } else {
            Icon icono = VaadinIcon.CLOSE_CIRCLE_O.create();
            icono.setColor("#fd2c2c");
            icono.setClassName("dialog-icon");
            HorizontalLayout layout = new HorizontalLayout(icono, paragraph);
            layout.getStyle().set("gap", "30px");
            dialogLayout.add(layout);

            dialog.getFooter().add(cerrarButton);
        }

        dialog.add(dialogLayout);
        dialog.open();
    }

    private HorizontalLayout getReservasPausadasContainer() {
        HorizontalLayout result = new HorizontalLayout();
        result.addClassName("reservas-pausadas-container");

        Icon atencionIcon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        atencionIcon.setClassName("atencion-icon");
        atencionIcon.setColor("#ff6868");
        Span estado = new Span(getTranslation("view.misreservas.reservaspausadas"));
        estado.setClassName("pausar-estado");
        Span limitacion = new Span(getTranslation("view.misreservas.clientesnopodran"));
        limitacion.setClassName("pausar-limitacion");
        HorizontalLayout pausaTextos = new HorizontalLayout(estado, limitacion);
        pausaTextos.setClassName("pausa-textos");
        HorizontalLayout pausaContainerContent = new HorizontalLayout(atencionIcon, pausaTextos);
        pausaContainerContent.setClassName("pausa-content");
        Button pausarReservasButton = new Button(getTranslation("view.misreservas.activarreservas"));
        pausarReservasButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        pausarReservasButton.setClassName("pausa-button");
        pausarReservasButton.addClickListener(event -> {
            setPausado(false);
        });

        result.add(pausaContainerContent, pausarReservasButton);

        return result;
    }

    private HorizontalLayout getReservasActivadasContainer() {
        HorizontalLayout result = new HorizontalLayout();
        result.addClassName("reservas-activadas-container");

        Button pausarReservasButton = new Button(getTranslation("view.misreservas.pausarreservas"));
        pausarReservasButton.setClassName("pausa-button");
        pausarReservasButton.addClickListener(event -> {
            setPausado(true);
        });

        result.add(pausarReservasButton);

        return result;
    }

    private void setPausado(Boolean pausar) {
        if (pausar) {
            restauranteService.pausarRestaurante(restaurante);
        } else {
            restauranteService.activarRestaurante(restaurante);
        }
        reservasPausadasContainer.setVisible(pausar);
        reservasActivadasContainer.setVisible(!pausar);
    }

    private VerticalLayout getEmptyState(String tipo) {
        Image emptyState = new Image();
        emptyState.setClassName("empty-state-image");
        emptyState.setSrc(new StreamResource("empty-box.png",
                () -> getClass().getResourceAsStream("/META-INF.resources/images/empty-box.png")
        ));

        H2 text = new H2(tipo.equals(getTranslation("view.misreservas.pendientes")) ? getTranslation(
                "view.misreservas.notenespendientes") : getTranslation("view.misreservas.notenespasadas"));
        text.setClassName("empty-state-text");
        VerticalLayout emptyStateContainer = new VerticalLayout(emptyState, text);

        emptyStateContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        emptyStateContainer.setAlignItems(Alignment.CENTER);
        emptyStateContainer.setClassName("empty-state-container");

        return emptyStateContainer;
    }

    private void updateGrids() {
        this.pendientesGrid.getDataProvider().refreshAll();
        this.pasadasGrid.getDataProvider().refreshAll();

        pendientesComponent.setVisible(!reservasPendientesList.isEmpty());
        pendientesEmptyState.setVisible(reservasPendientesList.isEmpty());

        pasadasComponent.setVisible(!reservasPasadasList.isEmpty());
        pasadasEmptyState.setVisible(reservasPasadasList.isEmpty());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }

    private static class ReservaFilter {
        private final GridListDataView<Reserva> dataView;

        private String nombre;
        private String dia;
        private String horario;
        private String cantidadPersonas;

        public ReservaFilter(GridListDataView<Reserva> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
            this.dataView.refreshAll();
        }

        public void setDia(String dia) {
            this.dia = dia;
            this.dataView.refreshAll();
        }

        public void setHorario(String horario) {
            this.horario = horario;
            this.dataView.refreshAll();
        }

        public void setCantidadPersonas(String cantidadPersonas) {
            this.cantidadPersonas = cantidadPersonas;
            this.dataView.refreshAll();
        }

        public boolean test(Reserva reserva) {
            boolean matchesNombre = matches(
                    reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido(), nombre);
            boolean matchesDia = matches(reserva.getFecha().format(DateTimeFormatter.ofPattern("dd/MM")), dia);
            boolean matchesHora = matches(
                    reserva.getHorario().getHorario().format(DateTimeFormatter.ofPattern("kk:mm")), horario);
            boolean matchesCantidadPersonas = matches(reserva.getCantidadPersonas().toString(), cantidadPersonas);

            return matchesNombre && matchesDia && matchesHora && matchesCantidadPersonas;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
