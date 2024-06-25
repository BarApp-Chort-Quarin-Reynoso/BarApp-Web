package com.barapp.web.views.dialogs;

import com.barapp.web.model.CalificacionPromedio;
import com.barapp.web.model.DetalleRestaurante;
import com.barapp.web.views.components.EdicionCaracteristicaField;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditorCaracteristicasOpinionDialog extends Dialog {
    final List<String> caracteristicasSeleccionables;
    final Map<String, CalificacionPromedio> caracteristicas;

    final Button agregarCaracteristicaButton;

    final Button guardarButton;
    final Button cancelarButton;

    final VerticalLayout caracteristicasLayout;

    final List<EdicionCaracteristicaField> edicionCaracteristicaFields = new ArrayList<>();

    final Span errorLabel;

    public EditorCaracteristicasOpinionDialog(DetalleRestaurante detalleRestaurante, List<String> caracteristicasSeleccionables) {
        this.caracteristicasSeleccionables = caracteristicasSeleccionables;
        this.caracteristicas = new LinkedHashMap<>(detalleRestaurante.getCaracteristicas());

        agregarCaracteristicaButton = new Button(
                getTranslation("comp.editorcaracteristicasdialog.agregarcaracteristica"));

        guardarButton = new Button(getTranslation("commons.save"));
        cancelarButton = new Button(getTranslation("commons.cancel"), ce -> close());
        caracteristicasLayout = new VerticalLayout();
        errorLabel = new Span();

        configureUI();
        cargarCaracteristicas();
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    private void configureUI() {
        caracteristicasLayout.setPadding(false);
        caracteristicasLayout.setHeightFull();

        agregarCaracteristicaButton.addClickListener(ce -> {
            if (edicionCaracteristicaFields.size() >= 5) {
                Notification.show(getTranslation("comp.editorcaracteristicasdialog.maxcaracteristicas", 5));
            } else {
                EdicionCaracteristicaField field = new EdicionCaracteristicaField(caracteristicasSeleccionables);
                edicionCaracteristicaFields.add(field);
                caracteristicasLayout.add(field);
                field.addDeleteListener(e -> removeField(field));
            }
        });

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setAlignSelf(FlexComponent.Alignment.END, agregarCaracteristicaButton);
        content.add(agregarCaracteristicaButton, caracteristicasLayout, errorLabel);
        add(content);

        setHeaderTitle(getTranslation("comp.editorcaracteristicasdialog.titulo"));

        errorLabel.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.FontSize.XSMALL);
        errorLabel.setWidthFull();
        errorLabel.getStyle().set("padding-top", "var(--lumo-space-m)");

        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarButton.addClickListener(ce -> {
            boolean invalid = false;

            for (EdicionCaracteristicaField field : edicionCaracteristicaFields) {
                String caracteristica = field.getValue();
                if (caracteristica == null || caracteristica.isEmpty()) {
                    field.setInvalid(true);
                    field.setErrorMessage(getTranslation("comp.editorcaracteristicasdialog.caracteristicavacia"));
                    invalid = true;
                    continue;
                }

                for (EdicionCaracteristicaField field2 : edicionCaracteristicaFields) {
                    if (field != field2 && field.getValue().equals(field2.getValue())) {
                        field.setInvalid(true);
                        field.setErrorMessage(
                                getTranslation("comp.editorcaracteristicasdialog.caracteristicarepetida"));
                        invalid = true;
                        break;
                    }
                }

                if (!invalid && !caracteristicas.containsKey(caracteristica)) {
                    caracteristicas.put(caracteristica, new CalificacionPromedio(0.0, 0));
                }
            }

            if (!invalid) {
                this.fireEvent(new SaveEvent(this, true, caracteristicas));

                close();
            }
        });

        getFooter().add(cancelarButton, guardarButton);

        setMinWidth("250px");
        setWidth("400px");
        setMaxWidth("min(400px, 100%)");
        setMinHeight("500px");
    }

    private void cargarCaracteristicas() {
        caracteristicas.forEach((k, v) -> {
            EdicionCaracteristicaField field = new EdicionCaracteristicaField(caracteristicasSeleccionables);

            edicionCaracteristicaFields.add(field);
            caracteristicasLayout.add(field);

            field.setValue(k, v);
            field.addDeleteListener(e -> {
                removeField(field);
                removeCaracteristica(e.getBean());
            });
        });
    }

    private void removeField(EdicionCaracteristicaField field) {
        edicionCaracteristicaFields.remove(field);
        caracteristicasLayout.remove(field);
    }

    private void removeCaracteristica(String caracteristica) {
        if (caracteristicas.containsKey(caracteristica)
                && caracteristicas.get(caracteristica).getCantidadOpiniones() != 0
                && errorLabel.getChildren().findAny().isEmpty()) {
            errorLabel.add(getTranslation("comp.editorcaracteristicasdialog.avisoeliminarcaracteristica"));
        }

        caracteristicas.remove(caracteristica);
    }

    public static class SaveEvent extends CrudEvent<EditorCaracteristicasOpinionDialog, Map<String, CalificacionPromedio>> {
        public SaveEvent(EditorCaracteristicasOpinionDialog source, boolean fromClient, Map<String, CalificacionPromedio> bean) {
            super(source, fromClient, bean);
        }
    }
}
