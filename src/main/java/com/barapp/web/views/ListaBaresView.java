package com.barapp.web.views;

import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIcon;

import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.EstadoRestaurante;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.Rol;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@SuppressWarnings("serial")
@PageTitle("Bares")
@Route(value = "bares", layout = MainLayout.class)
@RolesAllowed(value = {"ADMIN"})
public class ListaBaresView extends VerticalLayout {
    public static final Rol rolAllowed = Rol.ADMIN;

    private Grid<Restaurante> baresGrid;
    private Div noRestaurantesLabel;

    private final RestauranteService restauranteService;

    public ListaBaresView(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;

        configurarUi();
    }

    private void configurarUi() {
        configurarGrid();

        noRestaurantesLabel = new Div(getTranslation("views.bares.sinresultados"));

        try {
            List<Restaurante> restaurantes = restauranteService
                .getAll()
                    .stream()
                    .sorted((r1, r2) -> r1.getEstado().compareTo(r2.getEstado()))
                    .toList();
            if (!restaurantes.isEmpty()) {
                baresGrid.setItems(restaurantes);
                add(baresGrid);
            } else {
                add(noRestaurantesLabel);
            }
        } catch (Exception e) {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setText(getTranslation("commons.errorgenerico"));
            e.printStackTrace();
        }
        setSizeFull();
    }

    private void configurarGrid() {
        baresGrid = new Grid<>();
        baresGrid.setSizeFull();
        baresGrid.setMultiSort(true);

        Column<Restaurante> nombreCol = baresGrid.addColumn(bar -> bar.getNombre());
        nombreCol.setHeader(getTranslation("views.bares.grid.nombre"));
        nombreCol.setSortable(true);
        nombreCol.setWidth("180px");
        nombreCol.setFlexGrow(2);

        Column<Restaurante> direccionCol = baresGrid.addColumn(bar -> bar.getUbicacion().getDireccion());
        direccionCol.setHeader(getTranslation("views.bares.grid.direccion"));
        direccionCol.setSortable(true);
        direccionCol.setWidth("200px");
        direccionCol.setFlexGrow(2);

        Column<Restaurante> correoCol = baresGrid.addColumn(bar -> bar.getCorreo());
        correoCol.setHeader(getTranslation("views.bares.grid.email"));
        correoCol.setSortable(true);
        correoCol.setWidth("180px");
        correoCol.setFlexGrow(2);

        Column<Restaurante> telefonoCol = baresGrid.addColumn(bar -> bar.getTelefono());
        telefonoCol.setHeader(getTranslation("views.bares.grid.telefono"));
        telefonoCol.setSortable(true);
        telefonoCol.setWidth("150px");
        telefonoCol.setFlexGrow(1);

        Column<Restaurante> estadoCol = baresGrid.addComponentColumn(this::getEstadoBadge);
        estadoCol.setHeader(getTranslation("views.bares.grid.estado"));
        estadoCol.setWidth("150px");
        estadoCol.setFlexGrow(1);
        estadoCol.setComparator((r1, r2) -> r1.getEstado().compareTo(r2.getEstado()));

        Column<Restaurante> actionCol = baresGrid.addComponentColumn(this::getActionColumn);
        actionCol.setWidth("124px");
        actionCol.setFlexGrow(0);
    }

    private Component getEstadoBadge(Restaurante restaurante) {
        if (restaurante.getEstado() == null) return null;

        Span badge = new Span();
        badge.getElement().getThemeList().add("badge");
        badge.add(getTranslation(restaurante.getEstado().getTranslationKey()));

        switch (restaurante.getEstado()) {
            case HABILITADO:
                badge.getElement().getThemeList().add("success");
                break;
            case RECHAZADO:
                badge.getElement().getThemeList().add("error");
                break;
            case ESPERANDO_HABILITACION:
                break;
            case PAUSADO:
                badge.getElement().getThemeList().add("contrast");
                break;
            default:
                badge = null;
        }

        return badge;
    }

    private Component getActionColumn(Restaurante restaurante) {
        if (restaurante.getEstado() != null && restaurante
            .getEstado()
                .equals(EstadoRestaurante.ESPERANDO_HABILITACION)) {
            Button aceptarButton = new Button(LineAwesomeIcon.CHECK_SOLID.create());
            aceptarButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            aceptarButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            aceptarButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            aceptarButton.setAriaLabel("Accept");
            aceptarButton.addClickListener(e -> aceptarRestaurante(restaurante));

            Button rechazarButton = new Button(LineAwesomeIcon.TIMES_SOLID.create());
            rechazarButton.addThemeVariants(ButtonVariant.LUMO_ICON);
            rechazarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            rechazarButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            rechazarButton.setAriaLabel("Reject");
            rechazarButton.addClickListener(e -> rechazarRestaurante(restaurante));

            return new HorizontalLayout(aceptarButton, rechazarButton);
        } else {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setHeight("var(--lumo-size-s)");
            layout.getStyle().setMargin("var(--lumo-space-xs)");
            return layout;
        }
    }

    private void rechazarRestaurante(Restaurante restaurante) {
        restauranteService.rechazarRestaurante(restaurante);
        baresGrid.getDataProvider().refreshAll();
    }

    private void aceptarRestaurante(Restaurante restaurante) {
        restauranteService.aceptarRestaurante(restaurante);
        baresGrid.getDataProvider().refreshAll();
    }
}
