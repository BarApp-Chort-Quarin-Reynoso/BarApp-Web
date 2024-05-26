package com.barapp.web.views;

import com.barapp.web.business.service.ConfiguradorHorarioService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.model.ConfiguradorHorario;
import com.barapp.web.model.ConfiguradorHorarioNoLaboral;
import com.barapp.web.model.ConfiguradorHorarioSemanal;
import com.barapp.web.model.Horario;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.views.components.VisualizadorHorarios;
import com.barapp.web.views.dialogs.EditorConfigurardorHorarioDialog;
import com.barapp.web.views.dialogs.EditorDiaNoLaboralDialog;
import com.barapp.web.views.dialogs.VisualizadorConfiguradorHorariosDialog;
import com.flowingcode.addons.ycalendar.MonthCalendar;
import com.flowingcode.addons.ycalendar.YearMonthField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import org.vaadin.lineawesome.LineAwesomeIcon;

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
    final ConfiguradorHorarioService configuradorHorarioService;
    final RestauranteService restauranteService;

    YearMonthField monthField;
    MonthCalendar calendar;
    VisualizadorHorarios visualizadorHorarios;
    H3 calendarioTitle = new H3(getTranslation("views.mishorarios.fechasdisponibles"));
    H3 horariosTitle = new H3(getTranslation("views.mishorarios.horariosdisponibles"));
    Button agregarHorarioButton;
    Button horarioNoLaborableButton;
    Button verHorariosButton;
    Map<LocalDate, List<Horario>> horarios = new HashMap<>();

    VisualizadorConfiguradorHorariosDialog visualizadorDialog;

    public MisHorariosView(SecurityService securityService, ConfiguradorHorarioService configuradorHorarioService, RestauranteService restauranteService) {
        this.securityService = securityService;
        this.configuradorHorarioService = configuradorHorarioService;
        this.restauranteService = restauranteService;

        configurarUI();
    }

    public void configurarUI() {
        monthField = new YearMonthField();
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
            } else if (horarios.containsKey(date) && !horarios.get(date).isEmpty()) {
                return "open";
            }
            return null;
        });
        calendar.addDateSelectedListener(event -> {
            if (!event.getDate().isBefore(LocalDate.now())) {
                visualizadorHorarios.setValue(horarios.getOrDefault(event.getDate(), List.of()));
            }
        });
        calendar.addClassName("calendar-dias-habilitado");
        calendar.setWidth("100%");

        visualizadorHorarios = new VisualizadorHorarios();

        calendarioTitle.addClassName(LumoUtility.FontSize.LARGE);
        horariosTitle.addClassName(LumoUtility.FontSize.LARGE);
        agregarHorarioButton = new Button(getTranslation("views.mishorarios.agregarhorario"));
        agregarHorarioButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        agregarHorarioButton.setIcon(LineAwesomeIcon.PLUS_SOLID.create());
        agregarHorarioButton.addClickListener(ce -> {
            String correo = securityService.getAuthenticatedUser().get().getUsername();
            ConfiguradorHorarioSemanal configuradorHorario = ConfiguradorHorarioSemanal.builder()
                    .correoRestaurante(securityService.getAuthenticatedUser().get().getUsername())
                    .build();
            EditorConfigurardorHorarioDialog editorDialog = new EditorConfigurardorHorarioDialog(
                    configuradorHorario,
                    configuradorHorarioService.getAllByCorreoRestaurante(correo)
            );
            editorDialog.addSaveListener(se -> guardarConfigurador(se.getBean()));
            editorDialog.open();
        });
        horarioNoLaborableButton = new Button(getTranslation("views.mishorarios.horarionolaborable"));
        horarioNoLaborableButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        horarioNoLaborableButton.addClickListener(ce -> {
            String correo = securityService.getAuthenticatedUser().get().getUsername();
            ConfiguradorHorarioNoLaboral configuradorHorario = ConfiguradorHorarioNoLaboral.builder()
                    .correoRestaurante(securityService.getAuthenticatedUser().get().getUsername())
                    .build();
            EditorDiaNoLaboralDialog editorDialog = new EditorDiaNoLaboralDialog(
                    configuradorHorario,
                    configuradorHorarioService.getAllNoLaboralByCorreoRestaurante(correo)
            );
            editorDialog.addSaveListener(se -> guardarConfigurador(se.getBean()));
            editorDialog.open();
        });
        verHorariosButton = new Button(getTranslation("views.mishorarios.verhorarios"));
        verHorariosButton.addClickListener(ce -> {
            String correo = securityService.getAuthenticatedUser().get().getUsername();
            List<ConfiguradorHorario> configuradores =
                    configuradorHorarioService.getAllByCorreoRestaurante(correo);
            visualizadorDialog = new VisualizadorConfiguradorHorariosDialog(configuradores);
            visualizadorDialog.addDeleteListener(de -> eliminarConfigurador(de.getBean()));
            visualizadorDialog.addEditListener(event -> {
                if (event.getBean() instanceof ConfiguradorHorarioNoLaboral noLaboral) {
                    EditorDiaNoLaboralDialog editorDialog = new EditorDiaNoLaboralDialog(
                            noLaboral,
                            configuradores.stream()
                                    .filter(c -> c instanceof ConfiguradorHorarioNoLaboral)
                                    .map(c -> (ConfiguradorHorarioNoLaboral) c)
                                    .toList()
                    );
                    editorDialog.addSaveListener(e -> guardarConfigurador(e.getBean()));
                    editorDialog.open();
                } else {
                    EditorConfigurardorHorarioDialog editorDialog = new EditorConfigurardorHorarioDialog(
                            event.getBean(), configuradores);
                    editorDialog.addSaveListener(e -> guardarConfigurador(e.getBean()));
                    editorDialog.open();
                }
            });
            visualizadorDialog.open();
        });

        setPadding(true);
        setSpacing(true);
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        VerticalLayout calendarioWrapper = new VerticalLayout(calendarioTitle, monthField, calendar);
        calendarioWrapper.setMinWidth("0");
        calendarioWrapper.getStyle().setFlexBasis("400px");
        VerticalLayout horariosWrapper = new VerticalLayout(horariosTitle, visualizadorHorarios);
        horariosWrapper.setMinWidth("0");
        horariosWrapper.getStyle().setFlexBasis("400px");
        horariosWrapper.getStyle().setFlexGrow("1");
        horariosWrapper.getStyle().setFlexShrink("1");

        HorizontalLayout visualizacionLayout = new HorizontalLayout();
        visualizacionLayout.setWidth("70%");
        visualizacionLayout.setPadding(true);
        visualizacionLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        visualizacionLayout.getStyle().set("gap", "var(--lumo-space-xl)");
        visualizacionLayout.getStyle().setBorder("1px solid var(--lumo-shade-10pct)");
        visualizacionLayout.add(calendarioWrapper, horariosWrapper);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setPadding(true);
        buttonsLayout.setSpacing(true);
        buttonsLayout.add(agregarHorarioButton, horarioNoLaborableButton, verHorariosButton);

        add(visualizacionLayout, buttonsLayout);

        actualizarMes(YearMonth.now());
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
            horarios = restauranteService.horariosEnMesDisponiblesSegunDiaHoraActual(u.getUsername(), yearMonth);
            calendar.refreshAll();
        });
        visualizadorHorarios.setValue(new ArrayList<>());
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
