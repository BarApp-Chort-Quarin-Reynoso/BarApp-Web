package com.barapp.web.views.dialogs;

import com.barapp.web.model.ConfiguradorHorarioNoLaboral;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditorDiaNoLaboralDialog extends Dialog {
    final H2 title;

    final Button cancelarButton;
    final Button guardarButton;

    final DatePicker datePicker;

    final Binder<ConfiguradorHorarioNoLaboral> binder = new Binder<>();
    final ConfiguradorHorarioNoLaboral bean;
    final List<ConfiguradorHorarioNoLaboral> noLaboralesExistentes;

    public EditorDiaNoLaboralDialog(
            ConfiguradorHorarioNoLaboral configuradorHorario,
            List<ConfiguradorHorarioNoLaboral> noLaboralesExistentes
    ) {
        this.bean = configuradorHorario;
        this.noLaboralesExistentes = noLaboralesExistentes;

        title = new H2(configuradorHorario.getFecha() == null ?
                getTranslation("comp.editardianolaboral.title.agregar") :
                getTranslation("comp.editardianolaboral.title.editar"));

        cancelarButton = new Button(getTranslation("commons.cancel"));
        guardarButton = new Button(getTranslation("commons.save"));

        datePicker = new DatePicker(getTranslation("commons.fecha"));

        configureUI();
        configurarButtonsActions();
        configurarBinder();
        binder.readBean(configuradorHorario);
    }

    public Registration addSaveListener(ComponentEventListener<EditorDiaNoLaboralDialog.SaveEvent> listener) {
        return addListener(EditorDiaNoLaboralDialog.SaveEvent.class, listener);
    }

    private void configureUI() {
        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        title.addClassName("dialog-title");

        datePicker.setMin(LocalDate.now());

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.add(title);
        getHeader().add(headerLayout);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.setPadding(false);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.add(datePicker);
        add(contentLayout);

        getFooter().add(cancelarButton, guardarButton);

        setWidth("300px");
        setHeight("250px");
    }

    private void configurarButtonsActions() {
        cancelarButton.addClickListener(e -> this.close());
        guardarButton.addClickListener(e -> {
            binder.validate();
            if (!binder.writeBeanIfValid(bean)) {
                return;
            }

            this.fireEvent(new EditorDiaNoLaboralDialog.SaveEvent(this, true, bean));
            this.close();
        });
    }

    private void configurarBinder() {
        binder.forField(datePicker)
                .asRequired(getTranslation("comp.editardianolaboral.fechaobligatoria"))
                .withValidator(
                        this::validateNoLaboralExistenteEnMismaFecha,
                        getTranslation("comp.editardianolaboral.yaexiste")
                )
                .bind(ConfiguradorHorarioNoLaboral::getFecha, ConfiguradorHorarioNoLaboral::setFecha);
    }

    private boolean validateNoLaboralExistenteEnMismaFecha(LocalDate fecha) {
        for (ConfiguradorHorarioNoLaboral existente : noLaboralesExistentes) {
            if ((bean.getFecha() == null || !fecha.isEqual(bean.getFecha()))
                    && existente.getFecha().isEqual(fecha)) {
                return false;
            }
        }

        return true;
    }

    public static class SaveEvent extends CrudEvent<EditorDiaNoLaboralDialog, ConfiguradorHorarioNoLaboral> {
        public SaveEvent(EditorDiaNoLaboralDialog source, boolean fromClient, ConfiguradorHorarioNoLaboral bean) {
            super(source, fromClient, bean);
        }
    }
}
