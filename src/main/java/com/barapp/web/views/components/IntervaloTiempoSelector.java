package com.barapp.web.views.components;

import com.barapp.web.model.IntervaloTiempo;
import com.barapp.web.model.Mesa;
import com.barapp.web.model.enums.TipoComida;
import com.flowingcode.vaadin.addons.badgelist.Badge;
import com.flowingcode.vaadin.addons.badgelist.BadgeList;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntervaloTiempoSelector extends CustomField<IntervaloTiempo> {
    final Button addHorarioButton;
    final TimePicker desdePicker;
    final TimePicker hastaPicker;
    final TimePicker horarioPicker;
    final CapacidadField capacidadField;
    BadgeList horariosBadgeList;
    HorizontalLayout contentLayout;
    HorizontalLayout horarioWrapper;
    final Binder<IntervaloTiempo> binder = new Binder<>();
    Set<LocalTime> horarios = new TreeSet<>();

    private final LocalTime MIN_HORARIO = LocalTime.of(7, 0);

    public IntervaloTiempoSelector(List<Mesa> capacidadPorDefecto) {
        desdePicker = new TimePicker(getTranslation("views.mishorarios.desde"));
        hastaPicker = new TimePicker(getTranslation("views.mishorarios.hasta"));
        horarioPicker = new TimePicker(getTranslation("views.mishorarios.horario"));
        capacidadField = new CapacidadField(getTranslation("comp.editarhorariodialog.lugaresdisponibles"));
        addHorarioButton = new Button();
        horariosBadgeList = new BadgeList(List.of());
        capacidadField.setCapacidadPorDefecto(capacidadPorDefecto);

        configurarUI();
        setBinding();
    }

    public boolean isAnyFieldEmpty() {
        return desdePicker.isEmpty() || hastaPicker.isEmpty();
    }

    public boolean isHorarioAgregado() {
        return !desdePicker.isEmpty() || !hastaPicker.isEmpty();
    }

    public boolean isListaHorariosVacia() {
        return horarios.isEmpty();
    }

    private void configurarUI() {
        addHorarioButton.addThemeVariants(
                ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        addHorarioButton.setIcon(LineAwesomeIcon.PLUS_SOLID.create());

        horarioPicker.setWidth("120px");
        horarioPicker.setEnabled(false);

        desdePicker.setMin(MIN_HORARIO);
        hastaPicker.setMin(MIN_HORARIO);

        desdePicker.setClearButtonVisible(true);
        desdePicker.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                hastaPicker.setMin(e.getValue());
                horarioPicker.setMin(e.getValue());
                removeHorariosInvalidos();
                if (hastaPicker.getValue() != null) {
                    horarioPicker.setEnabled(true);
                    setHorarioSeteado(true);
                }
            } else {
                hastaPicker.setMin(MIN_HORARIO);
                horarioPicker.setEnabled(false);
                clearListaHorarios();
                setHorarioSeteado(false);
            }
        });
        hastaPicker.setClearButtonVisible(true);
        hastaPicker.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                desdePicker.setMax(e.getValue());
                horarioPicker.setMax(e.getValue());
                removeHorariosInvalidos();
                if (desdePicker.getValue() != null) {
                    horarioPicker.setEnabled(true);
                    setHorarioSeteado(true);
                }
            } else {
                desdePicker.setMax(null);
                horarioPicker.setEnabled(false);
                clearListaHorarios();
                setHorarioSeteado(false);
            }
        });
        horarioPicker.setStep(Duration.ofMinutes(15));

        addHorarioButton.addClickListener(ce -> {
            if (!horarioPicker.isEmpty()) {
                horarios.add(horarioPicker.getValue());
                setListaHorarios(horarios);
            }
        });

        horarioWrapper = new HorizontalLayout();
        horarioWrapper.add(horarioPicker, addHorarioButton, horariosBadgeList);
        horarioWrapper.getThemeList().clear();
        horarioWrapper.getThemeList().add("spacing-s");
        horarioWrapper.setAlignItems(FlexComponent.Alignment.END);

        VerticalLayout fieldsWrapper = new VerticalLayout();
        fieldsWrapper.getThemeList().clear();
        fieldsWrapper.addClassName(LumoUtility.Padding.Horizontal.MEDIUM);
        fieldsWrapper.add(
                new HorizontalLayout(desdePicker, hastaPicker),
                horarioWrapper,
                capacidadField
        );

        contentLayout = new HorizontalLayout();
        contentLayout.add(fieldsWrapper);
        contentLayout.addClassName("intervaloTiempoSelector-content");

        setHorarioSeteado(false);

        add(contentLayout);
        addClassName("intervaloTiempoSelector");
    }

    private void setHorarioSeteado(boolean seteado) {
        addHorarioButton.setVisible(seteado);
        horarioWrapper.setVisible(seteado);
        capacidadField.setVisible(seteado);
    }

    private void setBinding() {
        binder.forField(desdePicker)
                .bind(IntervaloTiempo::getDesde, IntervaloTiempo::setDesde);
        binder.forField(hastaPicker)
                .bind(IntervaloTiempo::getHasta, IntervaloTiempo::setHasta);
        binder.forField(capacidadField)
                .bind(IntervaloTiempo::getMesas, IntervaloTiempo::setMesas);
        binder.addValueChangeListener(e -> updateValue());
    }

    private void setListaHorarios(Set<LocalTime> horarios) {
        this.horarios = horarios;
        horarioWrapper.remove(horariosBadgeList);
        horariosBadgeList = new BadgeList(
                horarios.stream().map(h -> {
                    Button button = new Button();
                    SvgIcon icon = LineAwesomeIcon.TIMES_SOLID.create();
                    button.setIcon(icon);
                    button.addThemeVariants(
                            ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL,
                            ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST
                    );
                    button.addClickListener(e -> {
                        horarios.remove(h);
                        setListaHorarios(horarios);
                    });
                    Badge b = new Badge(new Div(h.toString()), button);
                    b.addThemeName("contrast");
                    b.addClassName("badge-con-boton-eliminar");
                    return b;
                }).toList());
        horariosBadgeList.addThemeName("contrast");
        horarioWrapper.add(horariosBadgeList);
        updateValue();
    }

    private void removeHorariosInvalidos() {
        horarios.removeIf(h -> h.isBefore(desdePicker.getValue()) || h.isAfter(hastaPicker.getValue()));
        setListaHorarios(horarios);
    }

    private void clearListaHorarios() {
        horarioPicker.setValue(null);
        horarios = new TreeSet<>();
        horarioWrapper.remove(horariosBadgeList);
        horariosBadgeList = new BadgeList(List.of());
        horarioWrapper.add(horariosBadgeList);
        updateValue();
    }

    @Override
    protected IntervaloTiempo generateModelValue() {
        IntervaloTiempo intTiempo = new IntervaloTiempo();
        if (desdePicker.getValue() != null && hastaPicker.getValue() != null) {
            intTiempo.setDesde(desdePicker.getValue());
            intTiempo.setHasta(hastaPicker.getValue());
            intTiempo.setHorarios(new ArrayList<>(horarios));
            intTiempo.setMesas(capacidadField.getValue());
            return intTiempo;
        } else {
            return null;
        }
    }

    @Override
    protected void setPresentationValue(IntervaloTiempo newValue) {
        if (newValue != null) {
            desdePicker.setValue(newValue.getDesde());
            hastaPicker.setValue(newValue.getHasta());
            capacidadField.setValue(newValue.getMesas());
            contentLayout.setVisible(true);
            setListaHorarios(new TreeSet<>(newValue.getHorarios()));
        }
    }
}
