package com.barapp.web.views.components;

import com.barapp.web.model.IntervaloTiempo;
import com.barapp.web.model.enums.TipoComida;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempoSelector extends CustomField<IntervaloTiempo> {
    H4 tipoComidaTitle;
    Button addHorarioButton;
    Button eliminarHorarioButton;
    TimePicker desdePicker;
    TimePicker hastaPicker;
    ComboBox<Integer> duracionReservaCombo;
    HorizontalLayout secondLineWrapper;
    Binder<IntervaloTiempo> binder = new Binder<>();

    public IntervaloTiempoSelector(TipoComida tipoComida) {
        tipoComidaTitle = new H4(getTranslation(tipoComida.getTranslationKey()));
        addHorarioButton = new Button();
        eliminarHorarioButton = new Button();
        desdePicker = new TimePicker(getTranslation("views.mishorarios.desde"));
        hastaPicker = new TimePicker(getTranslation("views.mishorarios.hasta"));
        duracionReservaCombo = new ComboBox<>(getTranslation("views.mishorarios.duracion"));

        configurarUI();
        configurarButtonsActions();
        setBinding();

        HorizontalLayout firstLineWrapper = new HorizontalLayout();
        firstLineWrapper.add(tipoComidaTitle, addHorarioButton);
        firstLineWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        secondLineWrapper = new HorizontalLayout();
        secondLineWrapper.add(desdePicker, hastaPicker, duracionReservaCombo, eliminarHorarioButton);
        secondLineWrapper.setAlignSelf(FlexComponent.Alignment.END, eliminarHorarioButton);
        secondLineWrapper.setVisible(false);

        add(firstLineWrapper, secondLineWrapper);
    }

    public boolean isFieldEmpty() {
        return desdePicker.isEmpty() || hastaPicker.isEmpty() || duracionReservaCombo.isEmpty();
    }

    public boolean isHorarioAgregado() {
        return secondLineWrapper.isVisible();
    }

    private void configurarUI() {
        addHorarioButton.addThemeVariants(
                ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        addHorarioButton.setIcon(LineAwesomeIcon.PLUS_SOLID.create());
        eliminarHorarioButton.addThemeVariants(
                ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        eliminarHorarioButton.setIcon(LineAwesomeIcon.TRASH_SOLID.create());

        desdePicker.setWidth("120px");
        hastaPicker.setWidth("120px");
        duracionReservaCombo.setItems(List.of(30, 45, 60, 90, 120));
        duracionReservaCombo.setItemLabelGenerator(i -> getTranslation("views.mishorarios.minutos", i));
        duracionReservaCombo.setWidth("120px");
    }

    private void configurarButtonsActions() {
        addHorarioButton.addClickListener(e -> {
            secondLineWrapper.setVisible(true);
            addHorarioButton.setVisible(false);
        });
        eliminarHorarioButton.addClickListener(e -> {
            secondLineWrapper.setVisible(false);
            addHorarioButton.setVisible(true);
            desdePicker.setValue(null);
            hastaPicker.setValue(null);
            duracionReservaCombo.setValue(null);
            updateValue();
        });
    }

    private void setBinding() {
        binder.forField(desdePicker)
                .asRequired()
                .bind(IntervaloTiempo::getDesde, IntervaloTiempo::setDesde);
        binder.forField(hastaPicker)
                .asRequired()
                .bind(IntervaloTiempo::getHasta, IntervaloTiempo::setHasta);
        binder.forField(duracionReservaCombo)
                .asRequired()
                .bind(IntervaloTiempo::getDuracionReserva, IntervaloTiempo::setDuracionReserva);
    }

    @Override
    protected IntervaloTiempo generateModelValue() {
        IntervaloTiempo intTiempo = new IntervaloTiempo();
        if (binder.writeBeanIfValid(intTiempo)) {
            return intTiempo;
        } else {
            return null;
        }
    }

    @Override
    protected void setPresentationValue(IntervaloTiempo newValue) {
        if (newValue != null) {
            binder.readBean(newValue);
            secondLineWrapper.setVisible(true);
            addHorarioButton.setVisible(false);
        }
    }
}
