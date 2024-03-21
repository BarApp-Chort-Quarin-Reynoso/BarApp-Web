package com.barapp.web.views.registro.formularios;

import com.barapp.web.business.interfaces.UbicacionService;
import com.barapp.web.model.Restaurante;
import com.barapp.web.views.utils.events.SiguienteFormularioEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.ArrayList;
import java.util.List;

public class FormularioInformacionBasica extends VerticalLayout {

    H3 tituloFormularioH3;
    FormLayout formInformacionBasicaLayout;
    TextField nombreBarTextfield;
    TextField direccionTextfield;
    IntegerField numeroTextfield;
    ComboBox<String> paisCombobox;
    ComboBox<String> provinciaCombobox;
    ComboBox<String> localidadCombobox;
    TextField correoTextfield;
    PasswordField contraseniaTextfield;
    PasswordField confirmarContraseniaTextfield;
    TextField telefonoTextfield;
    TextField cuitTextfield;

    HorizontalLayout botonesLayout;
    Button siguienteButton;
    Button cancelarButton;

    Binder<Restaurante> binder;

    List<SiguienteFormularioEvent> siguienteFormularioListeners;

    public FormularioInformacionBasica(Binder<Restaurante> binder) {
        this.binder = binder;
        this.siguienteFormularioListeners = new ArrayList<>();
        configurarElementos();
        configurarLayout();
        configurarBinders();
    }

    private void configurarElementos() {
        tituloFormularioH3 = new H3(getTranslation("views.registro.informacionobasica"));

        nombreBarTextfield = new TextField(getTranslation("views.registro.nombrebar"));
        direccionTextfield = new TextField(getTranslation("views.registro.direccion"));
        numeroTextfield = new IntegerField(getTranslation("views.registro.numero"));

        paisCombobox = new ComboBox<>(getTranslation("views.registro.pais"));
        paisCombobox.setItems(List.of("Argentina"));
        paisCombobox.setValue("Argentina");
        paisCombobox.setReadOnly(true);
        provinciaCombobox = new ComboBox<>(getTranslation("views.registro.provincia"));
        provinciaCombobox.setItems(List.of("Santa Fe"));
        provinciaCombobox.setValue("Santa Fe");
        provinciaCombobox.setReadOnly(true);
        localidadCombobox = new ComboBox<>(getTranslation("views.registro.localidad"));
        localidadCombobox.setItems(List.of("Santa Fe"));
        localidadCombobox.setValue("Santa Fe");
        localidadCombobox.setReadOnly(true);

        correoTextfield = new TextField(getTranslation("views.registro.correo"));
        contraseniaTextfield = new PasswordField(getTranslation("views.registro.contrasenia"));
        confirmarContraseniaTextfield = new PasswordField(getTranslation("views.registro.confirmacontrasenia"));
        telefonoTextfield = new TextField(getTranslation("views.registro.telefono"));
        cuitTextfield = new TextField(getTranslation("views.registro.cuit"));

        siguienteButton = new Button(getTranslation("commons.siguiente"));
        siguienteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        siguienteButton.addClickListener(e -> {


            Restaurante bean = new Restaurante();
            if (binder.writeBeanIfValid(bean)) {
                binder.setBean(bean);
                this.removeFromParent();
                for(SiguienteFormularioEvent listener : this.siguienteFormularioListeners) {
                    listener.onSiguienteFormularioEvent(bean);
                }
            }


        });

        cancelarButton = new Button(getTranslation("commons.cancel"));

        nombreBarTextfield.setWidthFull();
        direccionTextfield.setWidthFull();
        numeroTextfield.setWidthFull();
        paisCombobox.setWidthFull();
        provinciaCombobox.setWidthFull();
        localidadCombobox.setWidthFull();
        correoTextfield.setWidthFull();
        contraseniaTextfield.setWidthFull();
        confirmarContraseniaTextfield.setWidthFull();
        telefonoTextfield.setWidthFull();
        cuitTextfield.setWidthFull();
    }

    private void configurarLayout() {
        this.setPadding(false);
        this.setMargin(false);
        this.setId("registro-formulario-informacionbasica");

        HorizontalLayout ubicacionLayout = new HorizontalLayout(paisCombobox, provinciaCombobox, localidadCombobox);
        ubicacionLayout.setId("registro-ubicacion-layout");
        ubicacionLayout.setWidthFull();

        formInformacionBasicaLayout = new FormLayout(
                nombreBarTextfield,
                direccionTextfield, numeroTextfield,
                ubicacionLayout,
                correoTextfield,
                contraseniaTextfield, confirmarContraseniaTextfield,
                telefonoTextfield, cuitTextfield
        );
        formInformacionBasicaLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("670px", 2)
        );
        formInformacionBasicaLayout.setColspan(nombreBarTextfield, 2);
        formInformacionBasicaLayout.setColspan(ubicacionLayout, 2);
        formInformacionBasicaLayout.setColspan(correoTextfield, 2);

        botonesLayout = new HorizontalLayout(cancelarButton, siguienteButton);
        botonesLayout.setId("registro-botones-layout");
        botonesLayout.setWidthFull();

        this.add(tituloFormularioH3, formInformacionBasicaLayout, botonesLayout);
    }

    private void configurarBinders() {
        binder = new Binder<>(Restaurante.class);

        binder.forField(nombreBarTextfield).asRequired()
                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 3, 20),3, 20))
                .bind(Restaurante::getNombre, Restaurante::setNombre);

        binder.forField(direccionTextfield).asRequired()
                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 5, 30),5, 30))
                .bind(restaurante -> restaurante.getUbicacion().getCalle(), (restaurante, value) -> restaurante.getUbicacion().setCalle(value));

        binder.forField(numeroTextfield).asRequired()
                .withValidator(num -> num > 0, getTranslation("error.numeromayoracero"))
                .bind(restaurante -> restaurante.getUbicacion().getNumero(), (restaurante, value) -> restaurante.getUbicacion().setNumero(value));

        binder.forField(paisCombobox).asRequired().bind(restaurante -> restaurante.getUbicacion().getNombrePais(), (restaurante, value) -> restaurante.getUbicacion().setNombrePais(value));
        binder.forField(provinciaCombobox).asRequired().bind(restaurante -> restaurante.getUbicacion().getNombreProvincia(), (restaurante, value) -> restaurante.getUbicacion().setNombreProvincia(value));
        binder.forField(localidadCombobox).asRequired().bind(restaurante -> restaurante.getUbicacion().getNombreLocalidad(), (restaurante, value) -> restaurante.getUbicacion().setNombreLocalidad(value));

//        binder.forField(correoTextfield).asRequired()
//                .withValidator(new EmailValidator(getTranslation("error.mailnovalido")))
//                .bind(Restaurante::getCorreo, Restaurante::setCorreo);

//        binder.forField(contraseniaTextfield).asRequired().bind(Restaurante::getNombre, Restaurante::setNombre);
//
//        binder.forField(confirmarContraseniaTextfield).asRequired().bind(Restaurante::getNombre, Restaurante::setNombre);

//        binder.forField(telefonoTextfield).asRequired()
//                .withValidator(new RegexpValidator(getTranslation("error.telefono"), "^\\+?[0-9]+$"))
//                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 6, 14),6, 14))
//                .bind(Restaurante::getTelefono, Restaurante::setTelefono);
//
//        binder.forField(cuitTextfield).asRequired()
//                .withValidator(new RegexpValidator(getTranslation("error.formatocuit"),"^[0-9]{2}-[0-9]{8}-[0-9]{1}$"))
//                .bind(Restaurante::getCuit, Restaurante::setCuit);

    }

    public void addSiguienteFormularioListener(SiguienteFormularioEvent listener) {
        this.siguienteFormularioListeners.add(listener);
    }

}
