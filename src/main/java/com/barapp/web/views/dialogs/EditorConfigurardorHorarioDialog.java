package com.barapp.web.views.dialogs;

import com.barapp.web.model.*;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.views.components.CapacidadField;
import com.barapp.web.views.components.IntervaloTiempoSelector;
import com.barapp.web.views.utils.events.CrudEvent;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioDiaEspecificoValidator;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioSemanalValidator;
import com.barapp.web.views.utils.validation.ConfiguradorHorarioValidator;
import com.flowingcode.vaadin.addons.dayofweekselector.DayOfWeekSelector;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditorConfigurardorHorarioDialog extends Dialog {
    final H2 title;
    final Paragraph infotext;
    final Details details;

    final Button cancelarButton;
    final Button guardarButton;

    final DayOfWeekSelector dayOfWeekSelector;
    final DatePicker datePicker;
    final IntervaloTiempoSelector fieldDesayuno;
    final IntervaloTiempoSelector fieldAlmuerzo;
    final IntervaloTiempoSelector fieldMerienda;
    final IntervaloTiempoSelector fieldCena;

    final TabSheet tabsHorarios;
    final List<Tab> tabs;

    final Span errorLabel;

    final Binder<ConfiguradorHorario> binder = new Binder<>();
    final ConfiguradorHorario bean;
    final List<ConfiguradorHorario> horariosExistentes;
    ConfiguradorHorarioValidator validator;

    public EditorConfigurardorHorarioDialog(
            ConfiguradorHorario configuradorHorario,
            List<ConfiguradorHorario> horariosExistentes,
            List<Mesa> capacidadPorDefecto
    ) {
        bean = configuradorHorario;
        this.horariosExistentes = horariosExistentes;

        title = new H2(configuradorHorario.getHorarios().isEmpty() ?
                getTranslation("comp.editarhorariodialog.title.agregar") :
                getTranslation("comp.editarhorariodialog.title.editar"));
        infotext = new Paragraph();
        details = new Details();

        fieldDesayuno = new IntervaloTiempoSelector(capacidadPorDefecto);
        fieldAlmuerzo = new IntervaloTiempoSelector(capacidadPorDefecto);
        fieldMerienda = new IntervaloTiempoSelector(capacidadPorDefecto);
        fieldCena = new IntervaloTiempoSelector(capacidadPorDefecto);
        dayOfWeekSelector = new DayOfWeekSelector();
        datePicker = new DatePicker();
        cancelarButton = new Button(getTranslation("commons.cancel"));
        guardarButton = new Button(getTranslation("commons.save"));
        tabsHorarios = new TabSheet();
        tabs = new ArrayList<>();

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
        HorizontalLayout daysSelectorLayout = new HorizontalLayout();
        daysSelectorLayout.setWidthFull();
        daysSelectorLayout.setPadding(false);
        daysSelectorLayout.getStyle().setPaddingBottom("var(--lumo-space-m)");
        daysSelectorLayout.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
        daysSelectorLayout.getStyle().setAlignItems(Style.AlignItems.END);

        if (bean instanceof ConfiguradorHorarioSemanal) {
            dayOfWeekSelector.getStyle().setPadding("0");
            dayOfWeekSelector.setWeekDaysShort(List.of("D", "L", "M", "X", "J", "V", "S"));
            daysSelectorLayout.add(dayOfWeekSelector);

        } else {
            datePicker.setMin(LocalDate.now());
            daysSelectorLayout.add(datePicker);
        }

        cancelarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        errorLabel.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.FontSize.XSMALL);

        configurarInfoText();
        infotext.setWidthFull();
        infotext.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);
        infotext.getStyle().setPadding("var(--lumo-space-m)");
        infotext.getStyle().setMargin("0");
        infotext.getStyle().set("font-size", "var(--lumo-font-size-s)");

        details.setSummaryText("Información");
        details.add(infotext);

        tabs.add(tabsHorarios.add(getTranslation(TipoComida.DESAYUNO.getTranslationKey()), fieldDesayuno));
        tabs.add(tabsHorarios.add(getTranslation(TipoComida.ALMUERZO.getTranslationKey()), fieldAlmuerzo));
        tabs.add(tabsHorarios.add(getTranslation(TipoComida.MERIENDA.getTranslationKey()), fieldMerienda));
        tabs.add(tabsHorarios.add(getTranslation(TipoComida.CENA.getTranslationKey()), fieldCena));
        if (bean.getHorarios().get(TipoComida.DESAYUNO) != null) {
            tabsHorarios.setSelectedTab(tabs.get(0));
        } else if (bean.getHorarios().get(TipoComida.ALMUERZO) != null) {
            tabsHorarios.setSelectedTab(tabs.get(1));
        } else if (bean.getHorarios().get(TipoComida.MERIENDA) != null) {
            tabsHorarios.setSelectedTab(tabs.get(2));
        } else if (bean.getHorarios().get(TipoComida.CENA) != null) {
            tabsHorarios.setSelectedTab(tabs.get(3));
        }
        tabsHorarios.setWidthFull();


        fieldDesayuno.setWidthFull();
        fieldAlmuerzo.setWidthFull();
        fieldMerienda.setWidthFull();
        fieldCena.setWidthFull();

        VerticalLayout innerContentLayout = new VerticalLayout();
        innerContentLayout.add(daysSelectorLayout, tabsHorarios, errorLabel);
        innerContentLayout.setPadding(false);
        innerContentLayout.setWidth("80%");

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.add(
                details, innerContentLayout);
        contentLayout.setAlignItems(FlexComponent.Alignment.START);
        contentLayout.setMinWidth("450px");
        contentLayout.setWidthFull();
        contentLayout.setPadding(false);
        contentLayout.setAlignSelf(FlexComponent.Alignment.CENTER, innerContentLayout);
        add(contentLayout);

        title.addClassName("dialog-title");

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.add(title);
        getHeader().add(headerLayout);

        getFooter().add(cancelarButton, guardarButton);

        setWidth("60%");
    }

    private void configurarButtonsActions() {
        cancelarButton.addClickListener(e -> this.close());
        guardarButton.addClickListener(e -> {
            errorLabel.removeAll();
            binder.validate();
            if (!binder.writeBeanIfValid(bean)) {
                selectTabWithError();
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
        if (bean instanceof ConfiguradorHorarioSemanal) {
            addDaysOfWeekSelectorBinding();
        } else {
            addDatePickerBinding();
        }

        binder.forField(fieldDesayuno)
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || !fieldDesayuno.isAnyFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.fechadesdehastavacios")
                )
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldDesayuno.isHorarioAgregado() || !fieldDesayuno.isListaHorariosVacia(),
                        getTranslation("comp.editarhorariodialog.horariosvacio")
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
                        value -> !fieldAlmuerzo.isHorarioAgregado() || !fieldAlmuerzo.isAnyFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.fechadesdehastavacios")
                )
                .withValidator(
                        value -> !fieldAlmuerzo.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldAlmuerzo.isHorarioAgregado() || !fieldAlmuerzo.isListaHorariosVacia(),
                        getTranslation("comp.editarhorariodialog.horariosvacio")
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
                        value -> !fieldMerienda.isHorarioAgregado() || !fieldMerienda.isAnyFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.fechadesdehastavacios")
                )
                .withValidator(
                        value -> !fieldMerienda.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldMerienda.isHorarioAgregado() || !fieldMerienda.isListaHorariosVacia(),
                        getTranslation("comp.editarhorariodialog.horariosvacio")
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
                        value -> !fieldCena.isHorarioAgregado() || !fieldCena.isAnyFieldEmpty(),
                        getTranslation("comp.editarhorariodialog.fechadesdehastavacios")
                )
                .withValidator(
                        value -> !fieldCena.isHorarioAgregado() || value.getDesde().isBefore(value.getHasta()),
                        getTranslation("comp.editarhorariodialog.fechasincorrectas")
                )
                .withValidator(
                        value -> !fieldCena.isHorarioAgregado() || !fieldCena.isListaHorariosVacia(),
                        getTranslation("comp.editarhorariodialog.horariosvacio")
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

        binder.addValueChangeListener(e -> {
            errorLabel.removeAll();
        });
    }

    private void addDatePickerBinding() {
        binder.forField(datePicker)
                .withValidator(Objects::nonNull, "")
                .bind(
                        source -> ((ConfiguradorHorarioDiaEspecifico) bean).getFecha(),
                        (source, value) -> ((ConfiguradorHorarioDiaEspecifico) bean).setFecha(value)
                );
        validator = new ConfiguradorHorarioDiaEspecificoValidator((ConfiguradorHorarioDiaEspecifico) bean);
    }

    private void addDaysOfWeekSelectorBinding() {
        binder.forField(dayOfWeekSelector)
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

    private void selectTabWithError() {
        for (Tab t : tabs) {
            if (((HasValidation) tabsHorarios.getComponent(t)).isInvalid()) {
                tabsHorarios.setSelectedTab(t);
                break;
            }
        }
    }

    private void configurarInfoText() {
        infotext.add(getTranslation("comp.editarhorariodialog.info.text1"));
        infotext.getStyle().set("text-align", "justify");
        infotext
                .getElement()
                .appendChild(ElementFactory.createListItem(getTranslation("comp.editarhorariodialog.info.codicion1")));
        infotext.getElement()
                .appendChild(ElementFactory.createListItem(getTranslation("comp.editarhorariodialog.info.codicion2")));
    }

    public static class SaveEvent extends CrudEvent<EditorConfigurardorHorarioDialog, ConfiguradorHorario> {
        public SaveEvent(EditorConfigurardorHorarioDialog source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }
}
