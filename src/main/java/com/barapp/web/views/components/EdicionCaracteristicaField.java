package com.barapp.web.views.components;

import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.views.components.puntuacion.EstrellasPuntuacion;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EdicionCaracteristicaField extends CustomField<String> {
    final ComboBox<String> caracteristicaCB;
    final EstrellasPuntuacion puntuacion;
    final Button eliminarButton;
    final VerticalLayout opinionesLayout;

    public EdicionCaracteristicaField(List<String> caracteristicasSeleccionables) {
        caracteristicaCB = new ComboBox<>();
        caracteristicaCB.setItems(caracteristicasSeleccionables);
        puntuacion = new EstrellasPuntuacion();
        eliminarButton = new Button();
        opinionesLayout = new VerticalLayout();

        configureUI();
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    private void configureUI() {
        eliminarButton.addThemeVariants(
                ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        eliminarButton.setIcon(LineAwesomeIcon.TRASH_SOLID.create());
        eliminarButton.getStyle().setMarginLeft("auto");
        eliminarButton.addClickListener(ce -> {
            this.fireEvent(new DeleteEvent(this, true, caracteristicaCB.getValue()));
        });

        opinionesLayout.setPadding(false);
        opinionesLayout.setSpacing(false);
        opinionesLayout.add(puntuacion);
        opinionesLayout.setVisible(false);

        caracteristicaCB.addValueChangeListener(vce -> {
            this.setInvalid(false);
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-l");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(caracteristicaCB, opinionesLayout, eliminarButton);
        add(layout);
        setWidth("100%");
    }

    @Override
    protected String generateModelValue() {
        return getCaracteristica();
    }

    @Override
    protected void setPresentationValue(String newPresentationValue) {
        setCaracteristica(newPresentationValue, new CalificacionPromedio(0.0, 0));
    }

    public void setValue(String value, CalificacionPromedio calificacion) {
        setCaracteristica(value, calificacion);
        updateValue();
    }

    @Override
    public void setInvalid(boolean invalid) {
        if (caracteristicaCB.isVisible()) {
            super.setInvalid(invalid);
        }
    }

    private void setCaracteristica(String caracteristica, CalificacionPromedio calificacion) {
        caracteristicaCB.setValue(caracteristica);
        caracteristicaCB.setVisible(false);
        puntuacion.setValue(calificacion.getPuntuacion());
        puntuacion.setLabel(caracteristica);
        puntuacion.getStyle().set("padding-top", "0");
        if (calificacion.getCantidadOpiniones() == 0) {
            puntuacion.setSinOpiniones(true);
        }
        opinionesLayout.setVisible(true);
    }

    private String getCaracteristica() {
        return caracteristicaCB.getValue();
    }

    public static class DeleteEvent extends CrudEvent<EdicionCaracteristicaField, String> {
        public DeleteEvent(EdicionCaracteristicaField source, boolean fromClient, String bean) {
            super(source, fromClient, bean);
        }
    }
}
