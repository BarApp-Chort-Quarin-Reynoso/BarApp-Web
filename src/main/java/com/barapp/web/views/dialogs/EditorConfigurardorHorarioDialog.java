package com.barapp.web.views.dialogs;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioDiaEspecifico;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import com.barapp.web.model.IntervaloTiempo;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.model.enums.TipoConfigurador;
import com.barapp.web.views.components.IntervaloTiempoSelector;
import com.barapp.web.views.utils.events.CrudEvent;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioDiaEspecificoValidator;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioSemanalValidator;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioValidator;
import com.flowingcode.vaadin.addons.dayofweekselector.DayOfWeekSelector;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditorConfigurardorHorarioDialog extends Dialog {
    final H2 title;
    final SvgIcon infoIcon;

    final Button cancelarButton;
    final Button guardarButton;

    final ComboBox<TipoConfigurador> tipoConfiguradorCombo;
    final DayOfWeekSelector dayOfWeekSelector;
    final DatePicker datePicker;
    final IntervaloTiempoSelector fieldDesayuno;
    final IntervaloTiempoSelector fieldAlmuerzo;
    final IntervaloTiempoSelector fieldMerienda;
    final IntervaloTiempoSelector fieldCena;

    Binder.Binding<ConfiguradorHorario, LocalDate> datePickerBinding;
    Binder.Binding<ConfiguradorHorario, Set<DayOfWeek>> dayOfWeekSelectorBinding;

    final Span errorLabel;

    final Binder<ConfiguradorHorario> binder = new Binder<>();
    ConfiguradorHorario bean;
    ConfiguradorHorarioValidator validator;
    final List<ConfiguradorHorario> horariosExistentes;

    public EditorConfigurardorHorarioDialog(
            ConfiguradorHorario configuradorHorario,
            List<ConfiguradorHorario> horariosExistentes
    ) {
        bean = configuradorHorario;
        this.horariosExistentes = horariosExistentes;

        title = new H2(configuradorHorario.getHorarios().isEmpty() ?
                getTranslation("comp.editarhorariodialog.title.agregar") :
                getTranslation("comp.editarhorariodialog.title.editar"));
        infoIcon = LineAwesomeIcon.INFO_CIRCLE_SOLID.create();

        fieldDesayuno = new IntervaloTiempoSelector(TipoComida.DESAYUNO);
        fieldAlmuerzo = new IntervaloTiempoSelector(TipoComida.ALMUERZO);
        fieldMerienda = new IntervaloTiempoSelector(TipoComida.MERIENDA);
        fieldCena = new IntervaloTiempoSelector(TipoComida.CENA);
        tipoConfiguradorCombo = new ComboBox<>(getTranslation("comp.editarhorariodialog.tipoconfigurador"));
        dayOfWeekSelector = new DayOfWeekSelector();
        datePicker = new DatePicker();
        cancelarButton = new Button(getTranslation("commons.cancel"));
        guardarButton = new Button(getTranslation("commons.save"));

        errorLabel = new Span();

        configurarUI();
        configurarButtonsActions();
        configurarBinder();
        binder.readBean(configuradorHorario);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    private void configurarUI() {
        tipoConfiguradorCombo.setItems(List.of(TipoConfigurador.SEMANAL, TipoConfigurador.DIA_ESPECIFICO));
        tipoConfiguradorCombo.setItemLabelGenerator(t -> getTranslation(t.getTranslationKey()));
        tipoConfiguradorCombo.setValue(bean instanceof ConfiguradorHorarioSemanal
                ? TipoConfigurador.SEMANAL
                : TipoConfigurador.DIA_ESPECIFICO);
        tipoConfiguradorCombo.addValueChangeListener(e -> {
            if (e.getValue().equals(TipoConfigurador.SEMANAL)) {
                if (datePickerBinding != null) {
                    binder.removeBinding(datePickerBinding);
                    datePickerBinding = null;
                    bean = ConfiguradorHorarioSemanal.builder()
                            .horarios(bean.getHorarios())
                            .correoRestaurante(bean.getCorreoRestaurante())
                            .build();
                    addDaysOfWeekSelectorBinding();
                    binder.readBean(bean);
                }
            } else {
                if (dayOfWeekSelectorBinding != null) {
                    binder.removeBinding(dayOfWeekSelectorBinding);
                    dayOfWeekSelectorBinding = null;
                    bean = ConfiguradorHorarioDiaEspecifico.builder()
                            .fecha(LocalDate.now())
                            .horarios(bean.getHorarios())
                            .correoRestaurante(bean.getCorreoRestaurante())
                            .build();
                    addDatePickerBinding();
                    binder.readBean(bean);
                }
            }
        });

        dayOfWeekSelector.getStyle().setPadding("0");
        dayOfWeekSelector.setVisible(false);
        datePicker.setMin(LocalDate.now());
        datePicker.setVisible(false);
        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        errorLabel.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.FontSize.XSMALL);

        infoIcon.setTooltipText(getTranslation("comp.editarhorariodialog.info"));

        HorizontalLayout daysSelectorLayout = new HorizontalLayout();
        daysSelectorLayout.setWidthFull();
        daysSelectorLayout.setPadding(false);
        daysSelectorLayout.getStyle().setPaddingBottom("var(--lumo-space-m");
        daysSelectorLayout.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
        daysSelectorLayout.getStyle().setAlignItems(Style.AlignItems.END);
        daysSelectorLayout.add(tipoConfiguradorCombo, dayOfWeekSelector, datePicker);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.add(daysSelectorLayout, fieldDesayuno, fieldAlmuerzo, fieldMerienda, fieldCena, errorLabel);
        contentLayout.setAlignItems(FlexComponent.Alignment.START);
        contentLayout.setMaxWidth("500px");
        contentLayout.getStyle().setMargin("auto");
        add(contentLayout);

        title.addClassName("dialog-title");

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.add(title, infoIcon);
        getHeader().add(headerLayout);

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidthFull();
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footerLayout.add(cancelarButton, guardarButton);
        getFooter().add(footerLayout);


        setWidth("50%");
    }

    private void configurarButtonsActions() {
        cancelarButton.addClickListener(e -> this.close());
        guardarButton.addClickListener(e -> {
            errorLabel.removeAll();
            binder.validate();
            if (!binder.writeBeanIfValid(bean)) {
                return;
            }
            if (!validateBean(bean)) {
                return;
            }

            this.fireEvent(new SaveEvent(this, true, bean));
            this.close();
        });
    }

    private void configurarBinder() {
        if (tipoConfiguradorCombo.getValue().equals(TipoConfigurador.SEMANAL)) {
            addDaysOfWeekSelectorBinding();
        } else {
            addDatePickerBinding();
        }

        binder.forField(fieldDesayuno)
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || !fieldDesayuno.isFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.camposvacios")
                )
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || value
                                .getDesde()
                                .plusMinutes(value.getDuracionReserva())
                                .isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.duracionnoposibleparafechaseleccionadas")
                )
                .bind(
                        source -> source.getHorarios().get(TipoComida.DESAYUNO),
                        (source, value) -> {
                            if (value != null) {
                                source.getHorarios().put(TipoComida.DESAYUNO, value);
                            } else {
                                source.getHorarios().remove(TipoComida.DESAYUNO);
                            }
                        }
                );
        binder.forField(fieldAlmuerzo)
                .withValidator(
                        value -> !fieldAlmuerzo.isHorarioAgregado() || !fieldAlmuerzo.isFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.camposvacios")
                )
                .withValidator(
                        value -> !fieldAlmuerzo.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldAlmuerzo.isHorarioAgregado() || value
                                .getDesde()
                                .plusMinutes(value.getDuracionReserva())
                                .isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.duracionnoposibleparafechaseleccionadas")
                )
                .bind(
                        source -> source.getHorarios().get(TipoComida.ALMUERZO),
                        (source, value) -> {
                            if (value != null) {
                                source.getHorarios().put(TipoComida.ALMUERZO, value);
                            } else {
                                source.getHorarios().remove(TipoComida.ALMUERZO);
                            }
                        }
                );
        binder.forField(fieldMerienda)
                .withValidator(
                        value -> !fieldMerienda.isHorarioAgregado() || !fieldMerienda.isFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.camposvacios")
                )
                .withValidator(
                        value -> !fieldMerienda.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldMerienda.isHorarioAgregado() || value
                                .getDesde()
                                .plusMinutes(value.getDuracionReserva())
                                .isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.duracionnoposibleparafechaseleccionadas")
                )
                .bind(
                        source -> source.getHorarios().get(TipoComida.MERIENDA),
                        (source, value) -> {
                            if (value != null) {
                                source.getHorarios().put(TipoComida.MERIENDA, value);
                            } else {
                                source.getHorarios().remove(TipoComida.MERIENDA);
                            }
                        }
                );
        binder.forField(fieldCena)
                .withValidator(
                        value -> !fieldCena.isHorarioAgregado() || !fieldCena.isFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.camposvacios")
                )
                .withValidator(
                        value -> !fieldCena.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldCena.isHorarioAgregado() || value
                                .getDesde()
                                .plusMinutes(value.getDuracionReserva())
                                .isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.duracionnoposibleparafechaseleccionadas")
                )
                .bind(
                        source -> source.getHorarios().get(TipoComida.CENA),
                        (source, value) -> {
                            if (value != null) {
                                source.getHorarios().put(TipoComida.CENA, value);
                            } else {
                                source.getHorarios().remove(TipoComida.CENA);
                            }
                        }
                );
    }

    private void addDatePickerBinding() {
        dayOfWeekSelector.setVisible(false);
        datePicker.setVisible(true);
        datePickerBinding = binder.forField(datePicker)
                .withValidator(Objects::nonNull, "")
                .bind(
                        source -> ((ConfiguradorHorarioDiaEspecifico) bean).getFecha(),
                        (source, value) -> ((ConfiguradorHorarioDiaEspecifico) bean).setFecha(value)
                );
        validator = new ConfiguradorHorarioDiaEspecificoValidator((ConfiguradorHorarioDiaEspecifico) bean);
    }

    private void addDaysOfWeekSelectorBinding() {
        datePicker.setVisible(false);
        dayOfWeekSelector.setVisible(true);
        dayOfWeekSelectorBinding = binder.forField(dayOfWeekSelector)
                .withValidator(
                        value -> !value.isEmpty(),
                        getTranslation("comp.editarhorariodialog.seleccionaalmenosundia")
                )
                .bind(
                        source -> ((ConfiguradorHorarioSemanal) bean).getDaysOfWeek(),
                        (source, value) -> ((ConfiguradorHorarioSemanal) bean).setDaysOfWeek(value)
                );
        validator = new ConfiguradorHorarioSemanalValidator((ConfiguradorHorarioSemanal) bean);
    }

    private boolean validateBean(ConfiguradorHorario horario) {
        if (horario.getHorarios().isEmpty()) {
            errorLabel.add(getTranslation("comp.editarhorariodialog.almenosunhorario"));
            return false;
        }

        for (IntervaloTiempo it1 : horario.getHorarios().values()) {
            for (IntervaloTiempo it2 : horario.getHorarios().values()) {
                if (it1.equals(it2)
                        || it1.getHasta().equals(it2.getDesde())
                        || it1.getDesde().equals(it2.getHasta())) {
                    continue;
                }
                if (it1.getDesde().isAfter(it2.getDesde()) && it1.getDesde().isBefore(it2.getHasta())
                        || it1.getHasta().isAfter(it2.getDesde()) && it1.getHasta().isBefore(it2.getHasta())) {
                    errorLabel.add(getTranslation("comp.editarhorariodialog.horariossuperpuestos"));
                    return false;
                }
            }
        }

        for (ConfiguradorHorario cf : horariosExistentes) {
            if (cf.equals(horario)) {
                continue;
            }
            if (validator.colisiona(cf)) {
                errorLabel.add(getTranslation("comp.editarhorariodialog.configuradorcolisiona"));
                return false;
            }
        }

        return true;
    }

    public static class SaveEvent extends CrudEvent<EditorConfigurardorHorarioDialog, ConfiguradorHorario> {
        public SaveEvent(EditorConfigurardorHorarioDialog source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }
}
