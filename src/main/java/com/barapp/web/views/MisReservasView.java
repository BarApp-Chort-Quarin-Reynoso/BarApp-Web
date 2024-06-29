package com.barapp.web.views;

import com.barapp.web.model.Horario;
import com.barapp.web.model.Reserva;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.UsuarioApp;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.QRCodeGenerator;
import com.barapp.web.utils.UiUtils;
import com.google.zxing.WriterException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@PageTitle("Mis reservas")
@Route(value = "mis-reservas", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class MisReservasView extends VerticalLayout implements BeforeEnterObserver {

    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;

    Button generarQR;

    public MisReservasView(SecurityService securityService) {
        this.securityService = securityService;

        configurarElementos();
    }

    private void generarCodigoQR() {
        generarQR = new Button("Generar QR para concretar reserva");
        generarQR.addClickListener(event -> {
            Dialog dialog = new Dialog();

            dialog.setHeaderTitle("Confirmar reserva");

            String medium="https://localhost:8080/confirmar-reserva/numero";

            byte[] image = new byte[0];
            try {
                // Generate and Return Qr Code in Byte Array
                image = QRCodeGenerator.getQRCodeImage(medium,300,300);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }

            byte[] finalImage = image;
            Image qr = new Image(new StreamResource("", () -> new ByteArrayInputStream(finalImage)), "");

            dialog.add(qr);

            Button cerrarButton = new Button("Cerrar", e -> dialog.close());
            dialog.getFooter().add(cerrarButton);


            dialog.open();
        });
    }

    private void configurarElementos() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        tabSheet.add("Pendientes", getGridPendiente());
        tabSheet.add("Pasadas",
                new Div(new Text("This is the Payment tab content")));
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED);
        add(tabSheet);
    }

    private Component getGridPendiente() {
        Grid<Reserva> pendientesGrid = new Grid<>();
        pendientesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        Grid.Column<Reserva> nombreUsuarioColumn = pendientesGrid.addColumn(reserva -> reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido()).setSortable(true);
        Grid.Column<Reserva> diaColumn = pendientesGrid.addColumn(new LocalDateRenderer<>(Reserva::getFecha,"dd/MM")).setSortable(true).setComparator(Reserva::getFecha);
        Grid.Column<Reserva> horaColumn = pendientesGrid.addColumn(new LocalDateTimeRenderer<>(reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario()),"kk:mm")).setSortable(true).setComparator(Comparator.comparing(reserva -> LocalDateTime.of(reserva.getFecha(), reserva.getHorario().getHorario())));
        Grid.Column<Reserva> cantidadPersonasColumn = pendientesGrid.addColumn(Reserva::getCantidadPersonas).setSortable(true);

        List<Reserva> reservaList = new ArrayList<>();
        reservaList.add(new Reserva(false, 3, LocalDate.of(2024, 8, 19), new Horario(LocalTime.of(21, 30), TipoComida.CENA), new UsuarioApp("Valentin", "Chort", "", "", "", null), new Restaurante()));
        reservaList.add(new Reserva(false, 2, LocalDate.of(2024, 8, 22), new Horario(LocalTime.of(21, 0), TipoComida.CENA), new UsuarioApp("Saraceli", "Arina", "", "", "", null), new Restaurante()));
        reservaList.add(new Reserva(false, 5, LocalDate.of(2024, 8, 22), new Horario(LocalTime.of(9, 0), TipoComida.DESAYUNO), new UsuarioApp("Julio", "Quarin", "", "", "", null), new Restaurante()));
        reservaList.add(new Reserva(false, 10, LocalDate.of(2024, 9, 6), new Horario(LocalTime.of(13, 30), TipoComida.ALMUERZO), new UsuarioApp("Sagustina", "Ander", "", "", "", null), new Restaurante()));
        reservaList.add(new Reserva(false, 3, LocalDate.of(2024, 11, 15), new Horario(LocalTime.of(16, 5), TipoComida.MERIENDA), new UsuarioApp("Federico", "Reynoso", "", "", "", null), new Restaurante()));
        GridListDataView<Reserva> dataView = pendientesGrid.setItems(reservaList);
        ReservaFilter reservaFilter = new ReservaFilter(dataView);

        pendientesGrid.getHeaderRows().clear();
        HeaderRow headerRow = pendientesGrid.appendHeaderRow();

        headerRow.getCell(nombreUsuarioColumn).setComponent(UiUtils.createFilterHeader("Nombre usuario", reservaFilter::setNombre));
        headerRow.getCell(diaColumn).setComponent(UiUtils.createFilterHeader("Día", reservaFilter::setDia));
        headerRow.getCell(horaColumn).setComponent(UiUtils.createFilterHeader("Horario", reservaFilter::setHorario));
        headerRow.getCell(cantidadPersonasColumn).setComponent(UiUtils.createFilterHeader("Cantidad de personas", reservaFilter::setCantidadPersonas));
        pendientesGrid.addColumn(
                new ComponentRenderer<>(MenuBar::new, (menu, person) -> {
                    menu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
                    MenuItem qr = menu.addItem(new Icon(VaadinIcon.QRCODE), e -> {
                        System.out.println("GENERAR QR");
                    });
                    qr.getElement().setAttribute("title", "Concretar reserva");

                    MenuItem noCumplida = menu.addItem(new Icon(VaadinIcon.USER_CLOCK), e -> {
                        System.out.println("MARCAR COMO NO CUMPLIDA");
                    });
                    noCumplida.getElement().setAttribute("title", "Marcar como no cumplida");
                    MenuItem cancelar = menu.addItem(new Icon(VaadinIcon.CLOSE_CIRCLE_O), e -> {
                        System.out.println("CANCELAR");
                    });
                    cancelar.getElement().setAttribute("title", "Cancelar reserva");
                    cancelar.addClassNames(LumoUtility.Background.ERROR_10);
                })).setHeader("Acciones");

        pendientesGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        pendientesGrid.sort(List.of(new GridSortOrder<>(diaColumn, SortDirection.ASCENDING), new GridSortOrder<>(cantidadPersonasColumn, SortDirection.DESCENDING)));

        return pendientesGrid;
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
            boolean matchesNombre = matches(reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellido(), nombre);
            boolean matchesDia = matches(reserva.getFecha().format(DateTimeFormatter.ofPattern("dd/MM")), dia);
            boolean matchesHora = matches(reserva.getHorario().getHorario().format(DateTimeFormatter.ofPattern("kk:mm")), horario);
            boolean matchesCantidadPersonas = matches(reserva.getCantidadPersonas().toString(), cantidadPersonas);

            return matchesNombre && matchesDia && matchesHora && matchesCantidadPersonas;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }
}
