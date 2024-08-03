package com.barapp.web.views.dialogs;

import com.barapp.web.model.Mesa;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.*;

public class EditorCapacidadDialog extends Dialog {

    private List<Mesa> capacidadTotalEditable;

    private Grid.Column<Mesa> columnaTotal;

    public EditorCapacidadDialog(List<Mesa> capacidadTotalEditable) {
        this.capacidadTotalEditable = capacidadTotalEditable;

        this.setWidth("700px");
        this.setHeaderTitle(getTranslation("views.mishorarios.editarcapacidad"));

        Span errorSpan = new Span(getTranslation("error.mesasrepetidas"));
        errorSpan.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.FontSize.XSMALL);
        errorSpan.getStyle().set("visibility", "hidden");

        Button guardarButton = new Button(getTranslation("commons.save"));
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarButton.addClickListener(guardarEvent -> {
            if (validarCapacidadTotal(errorSpan, guardarButton)) {
                capacidadTotalEditable.sort(Comparator.comparing(Mesa::getCantidadDePersonasPorMesa));
                fireEvent(new SaveEvent(this, false, new LinkedHashSet<>(capacidadTotalEditable)));
                this.close();
            }
        });
        Button cancelarDialog = new Button(getTranslation("commons.cancel"), e -> this.close());
        cancelarDialog.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.getFooter().add(cancelarDialog, guardarButton);

        this.add(createDialogLayout(errorSpan, guardarButton), errorSpan);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    private VerticalLayout createDialogLayout(Span errorSpan, Button guardar) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setClassName("mi-bar-view-gestionar-capacidad-dialog");

        Grid<Mesa> capacidadGrid = new Grid<>();
        capacidadGrid.addClassName("capacidad-grid");

        Button agregarMesaButton = new Button(getTranslation("comp.editorcapacidad.agregarmesa"));
        agregarMesaButton.setClassName("mi-bar-view-gestionar-capacidad-dialog__button");
        agregarMesaButton.addClickListener(event -> {
            capacidadTotalEditable.add(new Mesa());
            capacidadGrid.getDataProvider().refreshAll();
            validarCapacidadTotal(errorSpan, guardar);
            actualizarFooter();
        });

        capacidadGrid.addComponentColumn(mesa -> {
                    IntegerField integerField = new IntegerField();
                    integerField.setValue(mesa.getCantidadMesas());
                    integerField.setStepButtonsVisible(true);
                    integerField.setMin(1);
                    integerField.addValueChangeListener(event -> {
                        if (event.getValue() < 1) {
                            integerField.setValue(event.getOldValue());
                            return;
                        }
                        mesa.setCantidadMesas(event.getValue());
                        actualizarFooter();
                    });
                    return integerField;
                })
                .setHeader(getTranslation("comp.editorcapacidad.cantidaddemesas"))
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        capacidadGrid.addComponentColumn(mesa -> {
                    IntegerField integerField = new IntegerField();
                    integerField.setValue(mesa.getCantidadDePersonasPorMesa());
                    integerField.setStepButtonsVisible(true);
                    integerField.setMin(1);
                    integerField.addValueChangeListener(event -> {
                        if (event.getValue() < 1) {
                            integerField.setValue(event.getOldValue());
                            return;
                        }
                        mesa.setCantidadDePersonasPorMesa(event.getValue());
                        validarCapacidadTotal(errorSpan, guardar);
                        actualizarFooter();
                    });
                    return integerField;
                })
                .setHeader(getTranslation("comp.editorcapacidad.cantidadpersonaspormesa"))
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        columnaTotal = capacidadGrid.addComponentColumn(mesa -> {
                    Button eliminarMesa = new Button(VaadinIcon.TRASH.create(), event -> {
                        capacidadTotalEditable.remove(mesa);
                        capacidadGrid.getDataProvider().refreshAll();
                        validarCapacidadTotal(errorSpan, guardar);
                        actualizarFooter();
                    });
                    eliminarMesa.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    return eliminarMesa;
                })
                .setAutoWidth(true);

        actualizarFooter();

        capacidadGrid.setItems(capacidadTotalEditable);
        capacidadGrid.setSelectionMode(Grid.SelectionMode.NONE);

        dialogLayout.add(agregarMesaButton, capacidadGrid);

        return dialogLayout;
    }

    private Boolean validarCapacidadTotal(Span errorSpan, Button guardar) {
        Set<Integer> cantidadPersonasSet = new HashSet<>();

        for (Mesa mesa : capacidadTotalEditable) {
            Integer cantidadDePersonas = mesa.getCantidadDePersonasPorMesa();
            if (cantidadPersonasSet.contains(cantidadDePersonas)) {
                errorSpan.getStyle().set("visibility", "visible");
                guardar.setEnabled(false);
                return false; // Se encontró una mesa repetida
            }
            cantidadPersonasSet.add(cantidadDePersonas);
        }

        errorSpan.getStyle().set("visibility", "hidden");
        guardar.setEnabled(true);
        return true;
    }

    private void actualizarFooter() {
        Div total = new Div(getTranslation("comp.editorcapacidad.total", capacidadTotalEditable.stream()
                .map(mesa -> mesa.getCantidadMesas() * mesa.getCantidadDePersonasPorMesa())
                .reduce(0, Integer::sum)));
        total.addClassNames(LumoUtility.TextAlignment.RIGHT, "vaadin-like-label");
        columnaTotal.setFooter(total);
    }

    public static class SaveEvent extends CrudEvent<EditorCapacidadDialog, Set<Mesa>> {
        public SaveEvent(EditorCapacidadDialog source, boolean fromClient, Set<Mesa> bean) {
            super(source, fromClient, bean);
        }
    }
}
