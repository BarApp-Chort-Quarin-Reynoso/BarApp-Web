package com.barapp.web.views;

import com.barapp.web.business.service.HorarioPorRestauranteService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.utils.Tuple;
import com.barapp.web.views.components.CapacidadField;
import com.barapp.web.views.components.VisualizadorHorarios;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.MainElement;
import com.barapp.web.views.dialogs.EditorConfigurardorHorarioDialog;
import com.barapp.web.views.dialogs.EditorDiaNoLaboralDialog;
import com.flowingcode.addons.ycalendar.MonthCalendar;
import com.flowingcode.addons.ycalendar.YearMonthField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Mis Horarios")
@Route(value = "mis-horarios", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MisHorariosView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    final SecurityService securityService;
    final RestauranteService restauranteService;
    final HorarioPorRestauranteService horarioPorRestauranteService;

    YearMonthField monthField;
    MonthCalendar calendar;
    VisualizadorHorarios visualizadorHorarios;
    H3 calendarioTitle = new H3(getTranslation("views.mishorarios.fechasdisponibles"));

    Restaurante restaurante;
    HorarioPorRestaurante horariosPorRestaurante;
    Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horarios = new HashMap<>();

    CapacidadField capacidadField;

    public MisHorariosView(SecurityService securityService, RestauranteService restauranteService, HorarioPorRestauranteService horarioPorRestauranteService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.horarioPorRestauranteService = horarioPorRestauranteService;

        configurarUI();

        try {
            restaurante = restauranteService
                    .getByCorreo(securityService.getAuthenticatedUser().orElseThrow().getUsername())
                    .orElseThrow();
            horariosPorRestaurante = horarioPorRestauranteService
                    .getByCorreoRestaurante(restaurante.getCorreo())
                    .orElseThrow();
            capacidadField.setValue(new ArrayList<>(horariosPorRestaurante.getMesas()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void configurarUI() {
        monthField = new YearMonthField();
        monthField.addClassName("monthField");
        monthField.setMin(YearMonth.now());
        monthField.addValueChangeListener(event -> actualizarMes(event.getValue()));
        monthField.getStyle().setWidth("100%");
        monthField.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        monthField.setI18n(FormatUtils.datePickerI18n());

        calendar = new MonthCalendar(YearMonth.now().plusMonths(1));
        calendar.setI18n(FormatUtils.datePickerI18n());
        calendar.setClassNameGenerator(date -> {
            if (date.isBefore(LocalDate.now())) {
                return "disabled";
            } else if (horarios.containsKey(date) && !horarios.get(date).getFirst().isEmpty()) {
                return "open";
            } else if (horarios.containsKey(date) && horarios
                    .get(date)
                    .getSecond() instanceof ConfiguradorHorarioNoLaboral) {
                return "holiday";
            }
            return null;
        });

        calendar.addDateSelectedListener(event -> {
            if (!event.getDate().isBefore(LocalDate.now())) {
                Tuple<List<Horario>, ConfiguradorHorario> horarios = this.horarios.get(event.getDate());
                visualizadorHorarios.setValue(
                        horarios != null ? horarios.getFirst() : new ArrayList<>(),
                        horarios != null ? horarios.getSecond() : null,
                        event.getDate()
                );
            } else {
                visualizadorHorarios.setValue(new ArrayList<>(), null, null);
            }
        });
        calendar.addClassName("calendarDiasHabilitado");
        calendar.setWidth("100%");

        capacidadField = new CapacidadField(getTranslation("views.mishorarios.capacidadpordefecto"));
        capacidadField.addSaveListener(e -> {
            guardarCapacidad(new ArrayList<>(e.getBean()));
        });

        visualizadorHorarios = new VisualizadorHorarios(
                getTranslation("views.mishorarios.horariosdisponibles"),
                securityService.getAuthenticatedUser().orElseThrow().getUsername()
        );
        visualizadorHorarios.setSizeFull();
        visualizadorHorarios.setFlexGrow(1);
        visualizadorHorarios.addEditListener(ce -> {
            List<ConfiguradorHorario> configuradores = new ArrayList<>(
                    horariosPorRestaurante.getConfiguradores().values());
            if (ce.getBean() instanceof ConfiguradorHorarioNoLaboral noLaboral) {
                EditorDiaNoLaboralDialog editorDialog = new EditorDiaNoLaboralDialog(
                        noLaboral,
                        configuradores.stream()
                                .filter(c -> c instanceof ConfiguradorHorarioNoLaboral)
                                .map(c -> (ConfiguradorHorarioNoLaboral) c)
                                .toList()
                );
                editorDialog.addSaveListener(e -> {
                    guardarConfigurador(e.getBean());
                });
                editorDialog.open();
            } else {
                EditorConfigurardorHorarioDialog editorDialog = new EditorConfigurardorHorarioDialog(
                        ce.getBean(), configuradores, horariosPorRestaurante.getMesas());
                editorDialog.addSaveListener(e -> {
                    guardarConfigurador(e.getBean());
                });
                editorDialog.open();
            }
        });
        visualizadorHorarios.addCreateListener(ce -> {
            if (ce.getBean() instanceof ConfiguradorHorarioNoLaboral) {
                EditorDiaNoLaboralDialog editorDialog = new EditorDiaNoLaboralDialog(
                        (ConfiguradorHorarioNoLaboral) ce.getBean(),
                        horariosPorRestaurante.getNoLaborales()
                );
                editorDialog.addSaveListener(se -> guardarConfigurador(se.getBean()));
                editorDialog.open();
            } else {
                EditorConfigurardorHorarioDialog editorDialog = new EditorConfigurardorHorarioDialog(
                        ce.getBean(),
                        horariosPorRestaurante.getLaborales(),
                        horariosPorRestaurante.getMesas()
                );
                editorDialog.addSaveListener(se -> guardarConfigurador(se.getBean()));
                editorDialog.open();
            }
        });
        visualizadorHorarios.addDeleteListener(de -> eliminarConfigurador(de.getBean()));

        calendarioTitle.addClassName(LumoUtility.FontSize.LARGE);

        VerticalLayout calendarioWrapper = new VerticalLayout(calendarioTitle, monthField, calendar);
        calendarioWrapper.setMinWidth("0");
        calendarioWrapper.getStyle().setFlexBasis("400px");
        visualizadorHorarios.setMinWidth("0");
        visualizadorHorarios.getStyle().setFlexBasis("400px");
        visualizadorHorarios.getStyle().setFlexGrow("1");
        visualizadorHorarios.getStyle().setFlexShrink("1");

        HorizontalLayout horariosLayout = new HorizontalLayout(calendarioWrapper, visualizadorHorarios);
        horariosLayout.addClassName("horariosLayout");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("visualizadorLayout");
        mainLayout.setPadding(true);
        mainLayout.add(horariosLayout, capacidadField);

        add(mainLayout);

        actualizarMes(YearMonth.now());
        MainElement mainElement = new MainElement(mainLayout);
        mainElement.addClassName("mis-horarios-view");

        this.add(mainElement, new BarappFooter());
        this.setPadding(false);
        this.setSpacing(false);
        this.setSizeFull();
    }

    private void guardarConfigurador(ConfiguradorHorario configuradorHorario) {
        try {
            horarioPorRestauranteService.saveConfigurador(
                    configuradorHorario, configuradorHorario.getId(), horariosPorRestaurante.getId());
            horariosPorRestaurante.getConfiguradores().put(configuradorHorario.getId(), configuradorHorario);
            actualizarMes(monthField.getValue());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void eliminarConfigurador(ConfiguradorHorario configuradorHorario) {
        try {
            horarioPorRestauranteService.deleteConfigurador(
                    configuradorHorario.getId(), horariosPorRestaurante.getId());
            horariosPorRestaurante.getConfiguradores().remove(configuradorHorario.getId());
            actualizarMes(monthField.getValue());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void actualizarMes(YearMonth yearMonth) {
        calendar.setYearMonth(yearMonth);
        securityService.getAuthenticatedUser().ifPresent(u -> {
            horarios = restauranteService.horariosEnMesDisponiblesSegunMesAnioConConfiguradorCoincidente(
                    u.getUsername(), yearMonth);
            calendar.refreshAll();
        });
        visualizadorHorarios.setValue(new ArrayList<>(), null, null);
    }

    private void guardarCapacidad(List<Mesa> mesas) {
        try {
            horarioPorRestauranteService.saveMesas(mesas, horariosPorRestaurante.getId());
            horariosPorRestaurante.setMesas(mesas);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
    }
}
