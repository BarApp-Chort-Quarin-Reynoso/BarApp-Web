package com.barapp.web.views.components;

import com.barapp.web.model.enums.TipoConfigurador;
import com.barapp.web.views.utils.events.CrudEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfiguradorButtonsLayout extends HorizontalLayout {
    final TipoConfigurador tipoConfigurador;

    final Button agregarBtn;
    final Button editarBtn;
    final Button eliminarBtn;

    public ConfiguradorButtonsLayout(TipoConfigurador tipoConfigurador) {
        this.tipoConfigurador = tipoConfigurador;
        agregarBtn = new Button();
        editarBtn = new Button();
        eliminarBtn = new Button();

        configurarUI();
    }

    public Registration addAgregarListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addEditarListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public Registration addEliminarListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    private void configurarUI() {
        agregarBtn.setIcon(LineAwesomeIcon.PLUS_SOLID.create());
        editarBtn.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        eliminarBtn.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());

        agregarBtn.addClickListener(ce -> fireEvent(new SaveEvent(this, false, tipoConfigurador)));
        editarBtn.addClickListener(ce -> fireEvent(new EditEvent(this, false, tipoConfigurador)));
        eliminarBtn.addClickListener(ce -> fireEvent(new DeleteEvent(this, false, tipoConfigurador)));

        Span span = new Span(getTranslation(tipoConfigurador.getTranslationKey()));
        span.setWidth("100px");

        this.add(span, agregarBtn, editarBtn, eliminarBtn);
        this.setAlignItems(Alignment.CENTER);
    }

    public static class SaveEvent extends CrudEvent<ConfiguradorButtonsLayout, TipoConfigurador> {
        public SaveEvent(ConfiguradorButtonsLayout source, boolean fromClient, TipoConfigurador bean) {
            super(source, fromClient, bean);
        }
    }

    public static class EditEvent extends CrudEvent<ConfiguradorButtonsLayout, TipoConfigurador> {
        public EditEvent(ConfiguradorButtonsLayout source, boolean fromClient, TipoConfigurador bean) {
            super(source, fromClient, bean);
        }
    }

    public static class DeleteEvent extends CrudEvent<ConfiguradorButtonsLayout, TipoConfigurador> {
        public DeleteEvent(ConfiguradorButtonsLayout source, boolean fromClient, TipoConfigurador bean) {
            super(source, fromClient, bean);
        }
    }
}
