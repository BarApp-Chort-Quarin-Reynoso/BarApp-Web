package com.barapp.web.views.components;

import com.barapp.web.model.Mesa;
import com.barapp.web.views.dialogs.EditorCapacidadDialog;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CapacidadField extends CustomField<List<Mesa>> {
    private List<Mesa> capacidadPorDefecto = new ArrayList<>();
    private List<Mesa> capacidad = new ArrayList<>();

    private final Span capacidadLabel = new Span();
    private final Span utilizaPorDefectoLabel = new Span(
            getTranslation("comp.editarhorariodialog.utilizacapacidadpordefecto"));
    private final Button editarButton = new Button();

    public CapacidadField() {
        editarButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editarButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editarButton.addClickListener(ce -> {
            if (capacidad.isEmpty()) {
                capacidad = capacidadPorDefecto.stream().map(Mesa::new).collect(Collectors.toList());
            }
            EditorCapacidadDialog dialog = new EditorCapacidadDialog(capacidad);
            dialog.addSaveListener(e -> {
                capacidad = new ArrayList<>(e.getBean());
                if (!capacidad.isEmpty() || capacidadPorDefecto.isEmpty()) {
                    setCapacidadLabel(capacidad);
                    utilizaPorDefectoLabel.setVisible(false);
                } else {
                    setCapacidadLabel(capacidadPorDefecto);
                    utilizaPorDefectoLabel.setVisible(true);
                }

                fireEvent(new SaveEvent(this, false, new ArrayList<>(e.getBean())));
                updateValue();
            });
            dialog.open();
        });

        utilizaPorDefectoLabel.addClassNames("empty-label", LumoUtility.FontSize.SMALL);
        utilizaPorDefectoLabel.setVisible(false);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-xl");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(capacidadLabel, editarButton, utilizaPorDefectoLabel);
        add(layout);
    }

    public CapacidadField(String label) {
        this();
        setLabel(label);
    }

    public void setCapacidadPorDefecto(List<Mesa> capacidadPorDefecto) {
        Objects.requireNonNull(capacidadPorDefecto, "Capacidad por defecto no puede ser nula");
        this.capacidadPorDefecto = capacidadPorDefecto;
        if (capacidad.isEmpty()) {
            setCapacidadLabel(capacidadPorDefecto);
            utilizaPorDefectoLabel.setVisible(true);
        }
    }

    @Override
    protected List<Mesa> generateModelValue() {
        return capacidad;
    }

    @Override
    protected void setPresentationValue(List<Mesa> newPresentationValue) {
        Objects.requireNonNull(newPresentationValue, "Capacidad no puede ser nula");
        capacidad = newPresentationValue;
        if (!capacidad.isEmpty() || capacidadPorDefecto.isEmpty()) {
            setCapacidadLabel(capacidad);
            utilizaPorDefectoLabel.setVisible(false);
        } else {
            setCapacidadLabel(capacidadPorDefecto);
            utilizaPorDefectoLabel.setVisible(true);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    private void setCapacidadLabel(List<Mesa> capacidad) {
        capacidadLabel.setText(capacidad.stream()
                .map(mesa -> mesa.getCantidadMesas() * mesa.getCantidadDePersonasPorMesa())
                .reduce(0, Integer::sum) + " personas");
    }

    public static class SaveEvent extends CrudEvent<CapacidadField, List<Mesa>> {
        public SaveEvent(CapacidadField source, boolean fromClient, List<Mesa> bean) {
            super(source, fromClient, bean);
        }
    }
}
