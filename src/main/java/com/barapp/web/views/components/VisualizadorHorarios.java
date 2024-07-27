package com.barapp.web.views.components;

import com.barapp.web.model.*;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.views.components.pageElements.ConfirmDeleteDialog;
import com.barapp.web.views.utils.events.CrudEvent;
import com.flowingcode.vaadin.addons.badgelist.Badge;
import com.flowingcode.vaadin.addons.badgelist.BadgeList;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDate;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisualizadorHorarios extends VerticalLayout {

    final VerticalLayout wrapper;
    final VerticalLayout horariosLayout = new VerticalLayout();
    final HorizontalLayout edicionButtonsLayout = new HorizontalLayout();
    final FlexLayout buttonsLayout = new FlexLayout();

    final Span infoLabel = new Span();
    final String emptyText = getTranslation("comp.visualizadorhorarios.emptytext");
    final String noDaySelectedText = getTranslation("comp.visualizadorhorarios.nodayselectedtext");

    final Map<TipoComida, List<String>> horarios = new LinkedHashMap<>();

    final Button agregarSemanalBtn = new Button(getTranslation("comp.visualizadorhorarios.agregarhorariosemanal"));
    final Button agregarEventoBtn = new Button(getTranslation("comp.visualizadorhorarios.agregarevento"));
    final Button agregarFeriadoBtn = new Button(getTranslation("comp.visualizadorhorarios.agregarferiado"));

    final Button editarBtn = new Button();
    final Button eliminarBtn = new Button();
    final Span configuradorAplicado = new Span();

    final String correoBar;
    ConfiguradorHorario configurador;
    LocalDate diaSeleccionado;

    public VisualizadorHorarios(String title, String correoBar) {
        this.correoBar = correoBar;

        H3 horariosTitle = new H3(title);
        horariosTitle.addClassName(LumoUtility.FontSize.LARGE);

        horariosLayout.setPadding(false);
        edicionButtonsLayout.setPadding(false);

        editarBtn.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        eliminarBtn.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        infoLabel.setText(noDaySelectedText);
        infoLabel.setClassName("empty-label");
        configuradorAplicado.addClassNames(LumoUtility.FontSize.SMALL);

        edicionButtonsLayout.add(configuradorAplicado, editarBtn, eliminarBtn);
        edicionButtonsLayout.setWidthFull();
        edicionButtonsLayout.setJustifyContentMode(JustifyContentMode.END);
        edicionButtonsLayout.setAlignItems(Alignment.CENTER);
        edicionButtonsLayout.setVisible(false);

        buttonsLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        buttonsLayout.getStyle().setMarginTop("auto");
        buttonsLayout.getStyle().setPadding("var(--lumo-space-m)");
        buttonsLayout.getStyle().set("gap", "var(--lumo-space-m)");

        wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.add(horariosLayout, edicionButtonsLayout, buttonsLayout);

        this.add(horariosTitle, infoLabel, wrapper);

        addClickListeners();
    }

    public void setValue(List<Horario> horarios, ConfiguradorHorario configurador, LocalDate diaSeleccionado) {
        this.horarios.clear();
        horariosLayout.removeAll();

        if (horarios.isEmpty()) {
            infoLabel.setText(emptyText);
            infoLabel.setVisible(true);
        } else {
            infoLabel.setVisible(false);

            for (Horario h : horarios) {
                this.horarios.computeIfAbsent(h.getTipoComida(), k -> new ArrayList<>());
                this.horarios.get(h.getTipoComida()).add(h.getHorario().format(FormatUtils.timeFormatter()));
            }

            for (Map.Entry<TipoComida, List<String>> e : this.horarios.entrySet()) {
                if (!e.getValue().isEmpty()) {
                    H4 tipoComidaTitle = new H4(getTranslation(e.getKey().getTranslationKey()));
                    tipoComidaTitle.addClassName(LumoUtility.FontSize.MEDIUM);

                    List<Badge> badges = new ArrayList<>();
                    e.getValue().forEach(h -> {
                        Badge badge = new Badge(h);
                        badge.addThemeName("contrast");
                        badges.add(badge);
                    });
                    BadgeList badgesHorarios = new BadgeList(badges);
                    badgesHorarios.setWidthFull();
                    badgesHorarios.addThemeName("contrast");

                    horariosLayout.add(tipoComidaTitle, badgesHorarios);
                }
            }
        }

        setConfigurador(configurador, diaSeleccionado);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addCreateListener(ComponentEventListener<CreateEvent> listener) {
        return addListener(CreateEvent.class, listener);
    }

    private void setConfigurador(ConfiguradorHorario configurador, LocalDate diaSeleccionado) {
        this.configurador = configurador;
        this.diaSeleccionado = diaSeleccionado;
        buttonsLayout.removeAll();
        edicionButtonsLayout.setVisible(false);

        if (diaSeleccionado != null) {
            if (configurador == null) {
                buttonsLayout.add(agregarSemanalBtn, agregarEventoBtn, agregarFeriadoBtn);
                edicionButtonsLayout.setVisible(false);
            } else {
                edicionButtonsLayout.setVisible(true);

                if (configurador instanceof ConfiguradorHorarioSemanal) {
                    buttonsLayout.add(agregarEventoBtn, agregarFeriadoBtn);
                    configuradorAplicado.setText(
                            getTranslation("comp.visualizadorhorarios.configuradorsemanalaplicado"));
                } else if (configurador instanceof ConfiguradorHorarioDiaEspecifico) {
                    configuradorAplicado.setText(
                            getTranslation("comp.visualizadorhorarios.configuradoreventoaplicado"));
                } else if (configurador instanceof ConfiguradorHorarioNoLaboral) {
                    configuradorAplicado.setText(
                            getTranslation("comp.visualizadorhorarios.configuradorferiadoaplicado"));
                } else {
                    throw new UnsupportedOperationException("Tipo de configurador no soportado");
                }
            }
        } else {
            infoLabel.setVisible(true);
            infoLabel.setText(noDaySelectedText);
        }
    }

    private void addClickListeners() {
        editarBtn.addClickListener(e -> fireEvent(new EditEvent(this, true, configurador)));
        eliminarBtn.addClickListener(e -> {
            ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(
                    getTranslation("comp.visualizadorconfiguradorhorarios.eliminardialog.title"),
                    getTranslation("comp.visualizadorconfiguradorhorarios.eliminardialog.text")
            );
            dialog.open();
            dialog.addConfirmListener(event -> {
                this.fireEvent(new DeleteEvent(this, true, configurador));
            });
        });
        agregarSemanalBtn.addClickListener(
                e -> fireEvent(new CreateEvent(this, true,
                        ConfiguradorHorarioSemanal.builder()
                                .daysOfWeek(Set.of(diaSeleccionado.getDayOfWeek()))
                                .build()
                )));
        agregarEventoBtn.addClickListener(
                e -> fireEvent(new CreateEvent(this, true,
                        ConfiguradorHorarioDiaEspecifico.builder()
                                .fecha(diaSeleccionado)
                                .build()
                )));
        agregarFeriadoBtn.addClickListener(
                e -> fireEvent(new CreateEvent(this, true,
                        ConfiguradorHorarioNoLaboral.builder()
                                .fecha(diaSeleccionado)
                                .build()
                )));
    }

    public static class EditEvent extends CrudEvent<VisualizadorHorarios, ConfiguradorHorario> {
        public EditEvent(VisualizadorHorarios source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }

    public static class DeleteEvent extends CrudEvent<VisualizadorHorarios, ConfiguradorHorario> {
        public DeleteEvent(VisualizadorHorarios source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }

    public static class CreateEvent extends CrudEvent<VisualizadorHorarios, ConfiguradorHorario> {
        public CreateEvent(VisualizadorHorarios source, boolean fromClient, ConfiguradorHorario bean) {
            super(source, fromClient, bean);
        }
    }
}
