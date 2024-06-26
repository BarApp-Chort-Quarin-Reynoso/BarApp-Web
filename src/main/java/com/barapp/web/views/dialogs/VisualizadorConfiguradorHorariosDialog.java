package com.barapp.web.views.dialogs;

import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioDiaEspecifico;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import com.barapp.web.model.enums.TipoConfigurador;
import com.barapp.web.utils.UiUtils;
import com.barapp.web.views.components.ConfirmDeleteDialog;
import com.barapp.web.views.utils.events.CrudEvent;
import com.flowingcode.vaadin.addons.dayofweekselector.DayOfWeekSelector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisualizadorConfiguradorHorariosDialog extends Dialog {
    final Button closeButton;
    final ComboBox<TipoConfigurador> tipoConfiguradorCombo;
    final VirtualList<ConfiguradorHorario> listaHorarios;
    final Span emptyLabel;
    ListDataProvider<ConfiguradorHorario> dataProvider;

    public VisualizadorConfiguradorHorariosDialog(List<ConfiguradorHorario> configuradoresHorario) {
        closeButton = new Button(LineAwesomeIcon.TIMES_SOLID.create(), e -> this.close());
        tipoConfiguradorCombo = new ComboBox<>(getTranslation("comp.editarhorariodialog.tipoconfigurador"));
        listaHorarios = new VirtualList<>();
        configurarVirtualList(configuradoresHorario);
        emptyLabel = new Span();

        configurarUI();
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public void actualizarElemento(ConfiguradorHorario configuradorHorario) {
        dataProvider.refreshItem(configuradorHorario);
    }

    private void configurarVirtualList(List<ConfiguradorHorario> configuradoresHorario) {
        dataProvider =
                DataProvider.ofCollection(configuradoresHorario);
        dataProvider.setFilter(configuradorHorario -> {
            if (tipoConfiguradorCombo.getValue() == null) {
                return true;
            }

            return configuradorHorario.getTipo().equals(tipoConfiguradorCombo.getValue());
        });
        listaHorarios.setDataProvider(dataProvider);
    }

    private void configurarUI() {
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        tipoConfiguradorCombo.setItems(TipoConfigurador.values());
        tipoConfiguradorCombo.setItemLabelGenerator(t -> getTranslation(t.getTranslationKey()));
        tipoConfiguradorCombo.setClearButtonVisible(true);
        tipoConfiguradorCombo.addValueChangeListener(vce -> {
            dataProvider.refreshAll();
            emptyLabel.removeAll();

            if (dataProvider.getItems().isEmpty()) {
                emptyLabel.setText(getTranslation("comp.visualizadorhorarios.ningunconfigurador"));
            } else if (vce.getValue() != null
                    && listaHorarios.getDataCommunicator().getItemCount() == 0) {
                emptyLabel.setText(
                        getTranslation("comp.visualizadorhorarios.ningunconfiguradordetipo"));
            }
        });
        dataProvider.addDataProviderListener(event -> {
            emptyLabel.removeAll();
            if (dataProvider.getItems().isEmpty()) {
                emptyLabel.setText(getTranslation("comp.visualizadorhorarios.ningunconfigurador"));
            }
        });
        if (dataProvider.getItems().isEmpty()) {
            emptyLabel.setText(getTranslation("comp.visualizadorhorarios.ningunconfigurador"));
        }
        emptyLabel.setClassName("empty-label");

        listaHorarios.setRenderer(getRenderer());
        listaHorarios.setHeight("400px");


        setHeaderTitle(getTranslation("views.mishorarios.verhorarios"));
        getHeader().add(closeButton);

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(false);
        contentLayout.setMaxWidth("600px");
        contentLayout.setMaxHeight("540px");
        contentLayout.getStyle().setMargin("auto");
        contentLayout.add(tipoConfiguradorCombo, emptyLabel, listaHorarios);

        add(contentLayout);
        setWidth("60%");
    }

    private ComponentRenderer<Component, ConfiguradorHorario> getRenderer() {
        return new ComponentRenderer<>(configuradorHorario -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button eliminarHorarioButton = new Button();
            eliminarHorarioButton.addThemeVariants(
                    ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            eliminarHorarioButton.setIcon(LineAwesomeIcon.TRASH_SOLID.create());
            eliminarHorarioButton.addClickListener(ce -> {
                ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(
                        getTranslation("comp.visualizadorconfiguradorhorarios.eliminardialog.title"),
                        getTranslation("comp.visualizadorconfiguradorhorarios.eliminardialog.text")
                );
                dialog.open();
                dialog.addConfirmListener(event -> {
                    this.fireEvent(new DeleteEvent(this, true, configuradorHorario));
                    dataProvider.getItems().remove(configuradorHorario);
                    dataProvider.refreshAll();
                });
            });

            Button editarHorarioButton = new Button();
            editarHorarioButton.addThemeVariants(
                    ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            editarHorarioButton.setIcon(LineAwesomeIcon.PEN_SOLID.create());
            editarHorarioButton.addClickListener(event -> {
                this.fireEvent(new EditEvent(this, true, configuradorHorario));
            });

            Component dataLayout = new VerticalLayout();
            if (configuradorHorario instanceof ConfiguradorHorarioSemanal semanal) {
                dataLayout = getLayoutConfiguradorSemanal(semanal);
            } else if (configuradorHorario instanceof ConfiguradorHorarioDiaEspecifico diaEspecifico) {
                dataLayout = getLayoutConfiguradorDiaEspecifico(diaEspecifico);
            } else if (configuradorHorario instanceof ConfiguradorHorarioNoLaboral noLaboral) {
                dataLayout = getLayoutConfiguradorNoLaboral(noLaboral);
            }

            layout.add(dataLayout, editarHorarioButton, eliminarHorarioButton);
            layout.setAlignItems(FlexComponent.Alignment.END);
            layout.setPadding(true);
            layout.getStyle().setBorder("1px solid var(--lumo-shade-10pct)");
            layout.getStyle().setMargin("var(--lumo-space-xs) 0");
            return layout;
        });
    }

    private Component getLayoutConfiguradorSemanal(ConfiguradorHorarioSemanal configurador) {
        VerticalLayout dataLayout = new VerticalLayout();
        dataLayout.setPadding(false);
        dataLayout.setSpacing(false);
        dataLayout.getThemeList().add("spacing-s");

        H3 title = new H3(getTranslation("configurador.semanal"));
        title.addClassName(LumoUtility.FontSize.LARGE);
        dataLayout.add(title);

        DayOfWeekSelector daysSelector = new DayOfWeekSelector();
        daysSelector.setValue(configurador.getDaysOfWeek());
        daysSelector.setReadOnly(true);
        daysSelector.getStyle().setPadding("0");
        daysSelector.setWeekDaysShort(List.of("D", "L", "M", "X", "J", "V", "S"));
        dataLayout.add(daysSelector);

        configurador.getHorarios().forEach(((tipoComida, intervaloTiempo) -> {
            VerticalLayout vl = new VerticalLayout();
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.getStyle().setPadding("var(--lumo-space-s) var(--lumo-space-m)");
            vl.getThemeList().add("spacing-s");

            HorizontalLayout intervaloLayout = new HorizontalLayout();
            intervaloLayout.setSpacing(false);
            intervaloLayout.getThemeList().add("spacing-xl");
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(intervaloTiempo.getDesde().toString()),
                    getTranslation("views.mishorarios.desde")
            ));
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(intervaloTiempo.getHasta().toString()),
                    getTranslation("views.mishorarios.hasta")
            ));
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(getTranslation("views.mishorarios.minutos", intervaloTiempo.getDuracionReserva())),
                    getTranslation("views.mishorarios.duracion")
            ));
            vl.add(new H4(getTranslation(tipoComida.getTranslationKey())), intervaloLayout);

            dataLayout.add(vl);
        }));

        return dataLayout;
    }

    private Component getLayoutConfiguradorDiaEspecifico(ConfiguradorHorarioDiaEspecifico configurador) {
        VerticalLayout dataLayout = new VerticalLayout();
        dataLayout.setPadding(false);
        dataLayout.setSpacing(false);
        dataLayout.getThemeList().add("spacing-s");

        H3 title = new H3(getTranslation("configurador.diaespecifico"));
        title.addClassName(LumoUtility.FontSize.LARGE);
        dataLayout.add(title);

        DatePicker datePicker = new DatePicker();
        datePicker.setReadOnly(true);
        datePicker.setValue(configurador.getFecha());
        dataLayout.add(datePicker);

        configurador.getHorarios().forEach(((tipoComida, intervaloTiempo) -> {
            VerticalLayout vl = new VerticalLayout();
            vl.setPadding(false);
            vl.setSpacing(false);
            vl.getStyle().setPadding("var(--lumo-space-s) var(--lumo-space-m)");
            vl.getThemeList().add("spacing-s");

            HorizontalLayout intervaloLayout = new HorizontalLayout();
            intervaloLayout.setSpacing(false);
            intervaloLayout.getThemeList().add("spacing-xl");
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(intervaloTiempo.getDesde().toString()),
                    getTranslation("views.mishorarios.desde")
            ));
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(intervaloTiempo.getHasta().toString()),
                    getTranslation("views.mishorarios.hasta")
            ));
            intervaloLayout.add(UiUtils.setTopLabelToComponent(
                    new Span(getTranslation("views.mishorarios.minutos", intervaloTiempo.getDuracionReserva())),
                    getTranslation("views.mishorarios.duracion")
            ));

            vl.add(new H4(getTranslation(tipoComida.getTranslationKey())), intervaloLayout);

            dataLayout.add(vl);
        }));

        return dataLayout;
    }

    private Component getLayoutConfiguradorNoLaboral(ConfiguradorHorarioNoLaboral noLaboral) {
        VerticalLayout dataLayout = new VerticalLayout();
        dataLayout.setPadding(false);
        dataLayout.setSpacing(false);
        dataLayout.getThemeList().add("spacing-s");
        dataLayout.getStyle().setBackground("var(--lumo-error-10pct)");

        H3 title = new H3(getTranslation("configurador.nolaboral"));
        title.addClassName(LumoUtility.FontSize.LARGE);
        dataLayout.add(title);

        DatePicker datePicker = new DatePicker();
        datePicker.setReadOnly(true);
        datePicker.setValue(noLaboral.getFecha());
        dataLayout.add(datePicker);

        return dataLayout;
    }

    public static class DeleteEvent extends CrudEvent<VisualizadorConfiguradorHorariosDialog, ConfiguradorHorario> {
        public DeleteEvent(VisualizadorConfiguradorHorariosDialog source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }

    public static class EditEvent extends CrudEvent<VisualizadorConfiguradorHorariosDialog, ConfiguradorHorario> {
        public EditEvent(VisualizadorConfiguradorHorariosDialog source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }
}
