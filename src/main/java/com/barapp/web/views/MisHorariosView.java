package com.barapp.web.views;

import com.barapp.web.business.service.ConfiguradorHorarioService;
import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.*;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.utils.Tuple;
import com.barapp.web.views.components.VisualizadorHorarios;
import com.barapp.web.views.components.pageElements.BarappFooter;
import com.barapp.web.views.components.pageElements.MainElement;
import com.barapp.web.views.dialogs.EditorCapacidadDialog;
import com.barapp.web.views.dialogs.EditorConfigurardorHorarioDialog;
import com.barapp.web.views.dialogs.EditorDiaNoLaboralDialog;
import com.flowingcode.addons.ycalendar.MonthCalendar;
import com.flowingcode.addons.ycalendar.YearMonthField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
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
import java.util.*;

@PageTitle("Mis Horarios")
@Route(value = "mis-horarios", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MisHorariosView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    final SecurityService securityService;
    final ConfiguradorHorarioService configuradorHorarioService;
    final RestauranteService restauranteService;
    final DetalleRestauranteService detalleRestauranteService;

    YearMonthField monthField;
    MonthCalendar calendar;
    VisualizadorHorarios visualizadorHorarios;
    H3 calendarioTitle = new H3(getTranslation("views.mishorarios.fechasdisponibles"));

    Restaurante restaurante;
    Map<LocalDate, Tuple<List<Horario>, ConfiguradorHorario>> horarios = new HashMap<>();

    Button gestionarCapacidadButton;
    private Set<Mesa> capacidadTotalOriginal;

    public MisHorariosView(SecurityService securityService, ConfiguradorHorarioService configuradorHorarioService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService) {
        this.securityService = securityService;
        this.configuradorHorarioService = configuradorHorarioService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;

        configurarUI();

        try {
            restaurante = restauranteService
                    .getByCorreo(securityService.getAuthenticatedUser().orElseThrow().getUsername())
                    .orElseThrow();
            restaurante.setDetalleRestaurante(detalleRestauranteService.get(restaurante.getIdDetalleRestaurante()));
            capacidadTotalOriginal = new HashSet<>();
            for (Mesa mesa : restaurante.getDetalleRestaurante().getCapacidadTotal()) {
                capacidadTotalOriginal.add(new Mesa(mesa));
            }
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

        gestionarCapacidadButton = new Button(getTranslation("views.mibar.gestionarcapacidad"));
        gestionarCapacidadButton.addClickListener(event -> {
            List<Mesa> capacidadTotalEditable = new ArrayList<>();
            for (Mesa mesa : capacidadTotalOriginal) {
                capacidadTotalEditable.add(new Mesa(mesa));
            }
            EditorCapacidadDialog dialog = new EditorCapacidadDialog(capacidadTotalEditable);
            dialog.open();

        });

        visualizadorHorarios = new VisualizadorHorarios(
                getTranslation("views.mishorarios.horariosdisponibles"),
                securityService.getAuthenticatedUser().orElseThrow().getUsername()
        );
        visualizadorHorarios.setSizeFull();
        visualizadorHorarios.setFlexGrow(1);
        visualizadorHorarios.addEditListener(ce -> {
            String correo = securityService.getAuthenticatedUser().orElseThrow().getUsername();
            List<ConfiguradorHorario> configuradores =
                    configuradorHorarioService.getAllByCorreoRestaurante(correo);
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
                        ce.getBean(), configuradores);
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
                        configuradorHorarioService.getAllNoLaboralByCorreoRestaurante(
                                ce.getBean().getCorreoRestaurante())
                );
                editorDialog.addSaveListener(se -> guardarConfigurador(se.getBean()));
                editorDialog.open();
            } else {
                EditorConfigurardorHorarioDialog editorDialog = new EditorConfigurardorHorarioDialog(
                        ce.getBean(),
                        configuradorHorarioService.getAllByCorreoRestaurante(ce.getBean().getCorreoRestaurante())
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
        mainLayout.add(horariosLayout, gestionarCapacidadButton);

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
            configuradorHorarioService.save(configuradorHorario, configuradorHorario.getId());
            actualizarMes(monthField.getValue());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void eliminarConfigurador(ConfiguradorHorario configuradorHorario) {
        try {
            configuradorHorarioService.delete(configuradorHorario.getId());
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
    }
}
