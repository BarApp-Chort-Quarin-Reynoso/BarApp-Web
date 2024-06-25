package com.barapp.web.views.components;

import com.barapp.web.model.CalificacionPromedio;
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
    final Span cantidadOpiniones;
    final Button eliminarButton;
    final VerticalLayout opinionesLayout;

    public EdicionCaracteristicaField(List<String> caracteristicasSeleccionables) {
        caracteristicaCB = new ComboBox<>();
        caracteristicaCB.setItems(caracteristicasSeleccionables);
        puntuacion = new EstrellasPuntuacion();
        cantidadOpiniones = new Span();
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

        cantidadOpiniones.addClassNames(LumoUtility.FontSize.SMALL);
        cantidadOpiniones.getStyle().set("font-style", "italic");
        cantidadOpiniones.getStyle().set("line-height", "var(--lumo-size-m)");

        opinionesLayout.setPadding(false);
        opinionesLayout.setSpacing(false);
        opinionesLayout.add(puntuacion, cantidadOpiniones);
        opinionesLayout.setVisible(false);

        caracteristicaCB.addValueChangeListener(vce -> {
            this.setInvalid(false);
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-l");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setWidth("250px");
        layout.add(caracteristicaCB, opinionesLayout, eliminarButton);
        add(layout);
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
        cantidadOpiniones.setText(calificacion.getCantidadOpiniones() == 1
                ? getTranslation("views.opiniones.cantidadopiniones.opinion")
                : getTranslation(
                "views.opiniones.cantidadopiniones.opiniones", calificacion.getCantidadOpiniones()));
        if (calificacion.getCantidadOpiniones() == 0) {
            puntuacion.setSinOpiniones(true);
            opinionesLayout.remove(cantidadOpiniones);
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
