package com.barapp.web.views;

import com.barapp.web.business.ImageContainer;
import com.barapp.web.business.service.DetalleRestauranteService;
import com.barapp.web.business.service.RestauranteService;
import com.barapp.web.business.service.UbicacionService;
import com.barapp.web.model.Mesa;
import com.barapp.web.model.Restaurante;
import com.barapp.web.model.enums.Rol;
import com.barapp.web.model.Ubicacion;
import com.barapp.web.security.SecurityService;
import com.barapp.web.utils.BarappUtils;
import com.barapp.web.views.components.Footer;
import com.barapp.web.views.components.MainElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.net.URL;

@SuppressWarnings("serial")
@PageTitle("Mi Bar")
@Route(value = "mi-bar", layout = MainLayout.class)
@RolesAllowed(value = {"BAR"})
public class MiBarView extends VerticalLayout implements BeforeEnterObserver {
    public static final Rol rolAllowed = Rol.BAR;

    private final SecurityService securityService;
    private final RestauranteService restauranteService;
    private final UbicacionService ubicacionService;
    private final DetalleRestauranteService detalleRestauranteService;

    // Información básica
    Section infoBasicaSection;
    H3 tituloInformacionBasica;
    TextField nombreBarTextfield;
    TextField correoTextfield;
    TextField telefonoTextfield;
    TextField cuitTextfield;

    // Detalles del bar
    Section detallesDelBarSection;
    H3 tituloDetallesDelBar;
    TextArea descripcionTextarea;
    TextField linkMenuTextfield;
    Button gestionarCapacidadButton;

    // Ubicación
    Section ubicacionSection;
    H3 tituloUbicacion;
    TextField direccionTextfield;
    IntegerField numeroTextfield;
    ComboBox<String> paisCombobox;
    ComboBox<String> provinciaCombobox;
    ComboBox<String> localidadCombobox;
    IFrame ubicacionIframe;

    // Imágenes
    Section imagenesSection;
    H3 tituloImagenes;
    Paragraph seleccionaLogo;
    Upload logoUpload;
    MemoryBuffer logoBuffer;
    Image logoImage;
    byte[] logoByteArray;
    String logoMimeType;
    Paragraph seleccionaPortada;
    Upload portadaUpload;
    MemoryBuffer portadaBuffer;
    Image portadaImage;
    byte[] portadaByteArray;
    String portadaMimeType;

    // Botones
    HorizontalLayout botonesLayout;
    Button cancelarButton;
    Button guardarButton;

    private final Binder<Restaurante> binder;
    private final Restaurante restaurante;
    private Set<Mesa> capacidadTotalOriginal;
    private List<Mesa> capacidadTotalEditable;

    private final Integer MAX_DESCRIPTION_LENGTH = 300;

    public MiBarView(SecurityService securityService, RestauranteService restauranteService, DetalleRestauranteService detalleRestauranteService, UbicacionService ubicacionService) {
        this.securityService = securityService;
        this.restauranteService = restauranteService;
        this.detalleRestauranteService = detalleRestauranteService;
        this.ubicacionService = ubicacionService;
        this.binder = new Binder<>(Restaurante.class);

        UserDetails userDetails = this.securityService.getAuthenticatedUser().orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        Optional<Restaurante> optRestaurante = this.restauranteService.getByCorreo(userDetails.getUsername());
        restaurante = optRestaurante.orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));
        try {
            restaurante.setDetalleRestaurante(detalleRestauranteService.get(restaurante.getIdDetalleRestaurante()));
            capacidadTotalOriginal = new HashSet<>();
            for (Mesa mesa : restaurante.getDetalleRestaurante().getCapacidadTotal()) {
                capacidadTotalOriginal.add(new Mesa(mesa));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);

        configurarElementos();
        configurarLayout();
        configurarBinders();
    }

    private void configurarElementos() {
        configurarInformacionBasica();
        configurarDetallesDelBar();
        configurarUbicacion();
        configurarImagenes();
        configurarBotones();
    }

    private void configurarInformacionBasica() {
        tituloInformacionBasica = new H3(getTranslation("views.registro.informacionobasica"));
        nombreBarTextfield = new TextField(getTranslation("views.registro.nombrebar"));
        correoTextfield = new TextField(getTranslation("views.registro.correo"));
        correoTextfield.setReadOnly(true);
        telefonoTextfield = new TextField(getTranslation("views.registro.telefono"));
        telefonoTextfield.setMaxLength(14);
        cuitTextfield = new TextField(getTranslation("views.registro.cuit"));
        infoBasicaSection = new Section(
                tituloInformacionBasica,
                nombreBarTextfield,
                correoTextfield,
                telefonoTextfield,
                cuitTextfield);
        infoBasicaSection.setClassName("info-basica-section");
    }

    private void configurarDetallesDelBar() {
        tituloDetallesDelBar = new H3(getTranslation("views.mibar.detallesdetubar"));

        descripcionTextarea = new TextArea(getTranslation("views.mibar.descripcion"));
        descripcionTextarea.setMaxLength(MAX_DESCRIPTION_LENGTH);
        descripcionTextarea.setValueChangeMode(ValueChangeMode.EAGER);
        descripcionTextarea.setHelperText("0/" + MAX_DESCRIPTION_LENGTH);
        descripcionTextarea.setHeight("250px");
        descripcionTextarea.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + MAX_DESCRIPTION_LENGTH));

        linkMenuTextfield = new TextField(getTranslation("views.mibar.linkmenu"));

        gestionarCapacidadButton = new Button(getTranslation("views.mibar.gestionarcapacidad"));
        gestionarCapacidadButton.addClickListener(event -> {
            capacidadTotalEditable = new ArrayList<>();
            for (Mesa mesa : capacidadTotalOriginal) {
                capacidadTotalEditable.add(new Mesa(mesa));
            }
            Dialog dialog = new Dialog();
            dialog.setWidth("700px");
            dialog.setHeaderTitle(getTranslation("views.mibar.gestionarcapacidad"));

            Span errorSpan = new Span(getTranslation("error.mesasrepetidas"));
            errorSpan.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.FontSize.XSMALL);
            errorSpan.getStyle().set("visibility", "hidden");

            Button guardarDialog = new Button(getTranslation("commons.save"));
            guardarDialog.addClickListener(guardarEvent -> {
                if (validarCapacidadTotal(errorSpan, guardarDialog)) {
                    capacidadTotalOriginal = new HashSet<>(capacidadTotalEditable);
                    dialog.close();
                }
            });
            Button cancelarDialog = new Button(getTranslation("commons.cancel"), e -> dialog.close());
            cancelarDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelarDialog, guardarDialog);

            dialog.add(createDialogLayout(errorSpan, guardarDialog), errorSpan);
            dialog.open();
        });

        detallesDelBarSection = new Section(
                tituloDetallesDelBar,
                descripcionTextarea,
                linkMenuTextfield,
                gestionarCapacidadButton);
        detallesDelBarSection.setClassName("detalle-bar-section");
    }

    private void configurarUbicacion() {
        tituloUbicacion = new H3(getTranslation("views.registro.ubicacion"));
        direccionTextfield = new TextField(getTranslation("views.registro.direccion"));
        direccionTextfield.setWidthFull();
        direccionTextfield.setValueChangeMode(ValueChangeMode.LAZY);
        direccionTextfield.addValueChangeListener(event -> {
            ubicacionIframe.setSrc(
                    ubicacionService.getMapUrl(
                            Ubicacion.builder()
                                    .calle(event.getValue())
                                    .numero(numeroTextfield.getValue())
                                    .nombreLocalidad(localidadCombobox.getValue())
                                    .nombreProvincia(provinciaCombobox.getValue())
                                    .nombrePais(paisCombobox.getValue())
                                    .build()
                    )
            );
        });
        numeroTextfield = new IntegerField(getTranslation("views.registro.numero"));
        numeroTextfield.setWidthFull();
        numeroTextfield.setValueChangeMode(ValueChangeMode.LAZY);
        numeroTextfield.addValueChangeListener(event -> {
            ubicacionIframe.setSrc(
                    ubicacionService.getMapUrl(
                            Ubicacion.builder()
                                    .calle(direccionTextfield.getValue())
                                    .numero(event.getValue())
                                    .nombreLocalidad(localidadCombobox.getValue())
                                    .nombreProvincia(provinciaCombobox.getValue())
                                    .nombrePais(paisCombobox.getValue())
                                    .build()
                    )
            );
        });
        paisCombobox = new ComboBox<>(getTranslation("views.registro.pais"));
        paisCombobox.setWidthFull();
        paisCombobox.setItems(List.of("Argentina"));
        paisCombobox.setValue("Argentina");
        paisCombobox.setReadOnly(true);
        provinciaCombobox = new ComboBox<>(getTranslation("views.registro.provincia"));
        provinciaCombobox.setWidthFull();
        provinciaCombobox.setItems(List.of("Santa Fe"));
        provinciaCombobox.setValue("Santa Fe");
        provinciaCombobox.setReadOnly(true);
        localidadCombobox = new ComboBox<>(getTranslation("views.registro.localidad"));
        localidadCombobox.setWidthFull();
        localidadCombobox.setItems(List.of("Santa Fe"));
        localidadCombobox.setValue("Santa Fe");
        localidadCombobox.setReadOnly(true);
        ubicacionIframe = new IFrame();
        ubicacionIframe.setSrc(ubicacionService.getMapUrl(restaurante.getUbicacion()));
        ubicacionIframe.setClassName("ubicacion-section__iframe");
        ubicacionIframe.setHeight("300px");

        HorizontalLayout direccionUbicacionLayout = new HorizontalLayout(direccionTextfield, numeroTextfield);
        direccionUbicacionLayout.setWidthFull();
        HorizontalLayout paisUbicacionLayout = new HorizontalLayout(paisCombobox, provinciaCombobox, localidadCombobox);
        paisUbicacionLayout.setWidthFull();

        ubicacionSection = new Section(
                tituloUbicacion,
                direccionUbicacionLayout,
                paisUbicacionLayout,
                ubicacionIframe
        );
        ubicacionSection.setClassName("ubicacion-section");
    }

    private void configurarImagenes() {
        tituloImagenes = new H3(getTranslation("views.registro.imagenes"));

        seleccionaLogo = new Paragraph(getTranslation("views.registro.selecionalogo"));

        logoImage = new Image();
        logoImage.setId("registro-logo-avatar");
        logoImage.setSrc(restaurante.getLogo());
        try {
            // Obtener la URL de la imagen desde el objeto Image
            URL logoUrl = new URL(logoImage.getSrc());
            // Convertir la URL a un InputStream
            InputStream inputStream = logoUrl.openStream();
            logoMimeType = BarappUtils.getMimeTypeFromStorageImage(restaurante.getLogo(), "image/png");
            logoByteArray = BarappUtils.convertInputStreamToByteArray(inputStream);
        } catch (Exception e) {
            // TODO: Romper todo si no anda
            throw new RuntimeException(e);
        }

        logoBuffer = new MemoryBuffer();
        logoUpload = new Upload(logoBuffer);
        logoUpload.setId("registro-upload-logo");
        logoUpload.setMaxFiles(1);
        logoUpload.setMaxFileSize(40960); // 40 KB
        logoUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        logoUpload.addSucceededListener(event -> {
            InputStream logoInputStream = logoBuffer.getInputStream();
            logoMimeType = event.getMIMEType();

            try {
                logoByteArray = BarappUtils.convertInputStreamToByteArray(logoInputStream);
                logoInputStream.reset();

                StreamResource streamResource = new StreamResource(restaurante.getId(), () -> logoInputStream);
                logoImage.setSrc(streamResource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        logoUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000, Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        seleccionaPortada = new Paragraph(getTranslation("views.registro.selecionaportada"));

        portadaImage = new Image();
        portadaImage.setId("registro-imagen-portada");
        portadaImage.setSrc(restaurante.getPortada());
        try {
            // Obtener la URL de la imagen desde el objeto Image
            URL portadaUrl = new URL(portadaImage.getSrc());
            // Convertir la URL a un InputStream
            InputStream inputStream = portadaUrl.openStream();
            portadaMimeType = BarappUtils.getMimeTypeFromStorageImage(restaurante.getPortada(), "image/jpeg");
            portadaByteArray = BarappUtils.convertInputStreamToByteArray(inputStream);
        } catch (Exception e) {
            // TODO: Romper todo si no anda
            throw new RuntimeException(e);
        }

        portadaBuffer = new MemoryBuffer();
        portadaUpload = new Upload(portadaBuffer);
        portadaUpload.setWidthFull();
        portadaUpload.setMaxFiles(1);
        portadaUpload.setMaxFileSize(5242880); // 5 MB
        portadaUpload.setAcceptedFileTypes("image/png", "image/jpeg");
        portadaUpload.addSucceededListener(event -> {
            InputStream portadaInputStream = portadaBuffer.getInputStream();
            portadaMimeType = event.getMIMEType();

            try {
                portadaByteArray = BarappUtils.convertInputStreamToByteArray(portadaInputStream);
                portadaInputStream.reset();

                StreamResource streamResource = new StreamResource(restaurante.getId(), () -> portadaInputStream);
                portadaImage.setSrc(streamResource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        portadaUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000, Notification.Position.BOTTOM_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        HorizontalLayout avatarLayout = new HorizontalLayout(logoImage);
        avatarLayout.setId("registro-avatar-layout");
        HorizontalLayout logoLayout = new HorizontalLayout(logoUpload, avatarLayout);
        logoLayout.setId("registro-logo-layout");
        logoLayout.setWidthFull();

        HorizontalLayout portadaLayout = new HorizontalLayout(portadaImage);
        portadaLayout.setId("registro-portada-layout");

        imagenesSection = new Section(
                tituloImagenes,
                seleccionaLogo,
                logoLayout,
                seleccionaPortada,
                portadaUpload,
                portadaLayout
        );
        imagenesSection.setClassName("imagenes-section");
    }

    private void configurarBotones() {
        guardarButton = new Button(getTranslation("commons.save"));
        guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarButton.addClickListener(e -> {
            if (binder.writeBeanIfValid(restaurante)) {
                try {
                    // Actualizar la capacidad total
                    restaurante.getDetalleRestaurante().setCapacidadTotal(capacidadTotalOriginal);

                    // Guardar imágenes en Firebase Storage
                    ImageContainer logo = new ImageContainer(new ByteArrayInputStream(logoByteArray), restaurante.getId(), logoMimeType);
                    ImageContainer portada = new ImageContainer(new ByteArrayInputStream(portadaByteArray), restaurante.getId(), portadaMimeType);

                    // Buscar ubicacion y pasar la info de latitud y longitud
                    ubicacionService.setLatitudLongitud(restaurante.getUbicacion());

                    // Guardar Restaurante y Detalle
                    restauranteService.saveConFotos(restaurante, logo, portada);
                    detalleRestauranteService.save(restaurante.getDetalleRestaurante(), restaurante.getIdDetalleRestaurante());

                    Notification notification = Notification.show(getTranslation("views.mibar.guardadoconexito"), 5000, Notification.Position.BOTTOM_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelarButton = new Button(getTranslation("commons.cancel"));
        cancelarButton.addClickListener(e -> {

        });

        botonesLayout = new HorizontalLayout(cancelarButton, guardarButton);
        botonesLayout.setId("registro-botones-layout");
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
    }

    private VerticalLayout createDialogLayout(Span errorSpan, Button guardar) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setClassName("mi-bar-view-gestionar-capacidad-dialog");

        Grid<Mesa> capacidadGrid = new Grid<>();

        Button agregarMesaButton = new Button(getTranslation("views.mibar.agregarmesa"));
        agregarMesaButton.setClassName("mi-bar-view-gestionar-capacidad-dialog__button");
        agregarMesaButton.addClickListener(event -> {
            capacidadTotalEditable.add(new Mesa());
            capacidadGrid.getDataProvider().refreshAll();
            validarCapacidadTotal(errorSpan, guardar);
        });

        capacidadGrid.addComponentColumn(mesa -> {
                    IntegerField integerField = new IntegerField();
                    integerField.setValue(mesa.getCantidadMesas());
                    integerField.setStepButtonsVisible(true);
                    integerField.setMin(1);
                    integerField.addValueChangeListener(event -> {
                        if (event.getValue() < 1) {
                            integerField.setValue(event.getOldValue());
                            return;
                        }
                        mesa.setCantidadMesas(event.getValue());
                    });
                    return integerField;
                })
                .setHeader(getTranslation("views.mibar.cantidaddemesas"))
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        capacidadGrid.addComponentColumn(mesa -> {
                    IntegerField integerField = new IntegerField();
                    integerField.setValue(mesa.getCantidadDePersonasPorMesa());
                    integerField.setStepButtonsVisible(true);
                    integerField.setMin(1);
                    integerField.addValueChangeListener(event -> {
                        if (event.getValue() < 1) {
                            integerField.setValue(event.getOldValue());
                            return;
                        }
                        mesa.setCantidadDePersonasPorMesa(event.getValue());
                        validarCapacidadTotal(errorSpan, guardar);
                    });
                    return integerField;
                })
                .setHeader(getTranslation("views.mibar.cantidadpersonaspormesa" ))
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        capacidadGrid.addComponentColumn(mesa -> {
                    Button eliminarMesa = new Button(VaadinIcon.TRASH.create(), event -> {
                        capacidadTotalEditable.remove(mesa);
                        capacidadGrid.getDataProvider().refreshAll();
                        validarCapacidadTotal(errorSpan, guardar);
                    });
                    eliminarMesa.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                    return eliminarMesa;
                })
                .setAutoWidth(true);

        capacidadGrid.setItems(capacidadTotalEditable);
        capacidadGrid.setSelectionMode(Grid.SelectionMode.NONE);

        dialogLayout.add(agregarMesaButton, capacidadGrid);

        return dialogLayout;
    }

    private Boolean validarCapacidadTotal(Span errorSpan, Button guardar) {
        Set<Integer> cantidadPersonasSet = new HashSet<>();

        for (Mesa mesa : capacidadTotalEditable) {
            Integer cantidadDePersonas = mesa.getCantidadDePersonasPorMesa();
            if (cantidadPersonasSet.contains(cantidadDePersonas)) {
                errorSpan.getStyle().set("visibility", "visible");
                guardar.setEnabled(false);
                return false; // Se encontró una mesa repetida
            }
            cantidadPersonasSet.add(cantidadDePersonas);
        }

        errorSpan.getStyle().set("visibility", "hidden");
        guardar.setEnabled(true);
        return true;
    }

    private void configurarLayout() {
        MainElement mainElement = new MainElement(
                infoBasicaSection,
                detallesDelBarSection,
                ubicacionSection,
                imagenesSection,
                botonesLayout
        );
        mainElement.addClassName("mi-bar-view");

        this.add(mainElement,new Footer());
    }

    private void configurarBinders() {
        // Información básica
        binder.forField(nombreBarTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 3, 40), 3, 40))
                .bind(Restaurante::getNombre, Restaurante::setNombre);

        binder.forField(correoTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new EmailValidator(getTranslation("error.mailnovalido")))
                .bind(Restaurante::getCorreo, Restaurante::setCorreo);

        binder.forField(telefonoTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new RegexpValidator(getTranslation("error.telefono"), "^\\+?[0-9]+$"))
                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 6, 14), 6, 14))
                .bind(Restaurante::getTelefono, Restaurante::setTelefono);

        binder.forField(cuitTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new RegexpValidator(getTranslation("error.formatocuit"), "^[0-9]{2}-[0-9]{8}-[0-9]{1}$"))
                .bind(Restaurante::getCuit, Restaurante::setCuit);

        // Detalles de tu bar
        binder.forField(descripcionTextarea)
                .withValidator(new StringLengthValidator(getTranslation("error.logitudmaxima", MAX_DESCRIPTION_LENGTH), 0,MAX_DESCRIPTION_LENGTH))
                .bind(restaurante -> restaurante.getDetalleRestaurante().getDescripcion(),(restaurante, value) -> restaurante.getDetalleRestaurante().setDescripcion(value));

        binder.forField(linkMenuTextfield)
                .withValidator(link -> link.isEmpty() || UrlValidator.getInstance().isValid(link), getTranslation("error.linkmenu"))
                .bind(restaurante -> restaurante.getDetalleRestaurante().getMenu(),(restaurante, value) -> restaurante.getDetalleRestaurante().setMenu(value));

        // Ubicacion
        binder.forField(direccionTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new StringLengthValidator(getTranslation("error.longitudtextonovalido", 5, 50), 5, 50))
                .bind(restaurante -> restaurante.getUbicacion().getCalle(), (restaurante, value) -> restaurante.getUbicacion().setCalle(value));

        binder.forField(numeroTextfield).asRequired(getTranslation("error.campoobligatorio"))
                .withValidator(new IntegerRangeValidator(getTranslation("error.numeroentre", 1, 1000000), 1, 1000000))
                .bind(restaurante -> restaurante.getUbicacion().getNumero(), (restaurante, value) -> restaurante.getUbicacion().setNumero(value));

        binder.forField(paisCombobox).asRequired(getTranslation("error.campoobligatorio")).bind(restaurante -> restaurante.getUbicacion().getNombrePais(), (restaurante, value) -> restaurante.getUbicacion().setNombrePais(value));
        binder.forField(provinciaCombobox).asRequired(getTranslation("error.campoobligatorio")).bind(restaurante -> restaurante.getUbicacion().getNombreProvincia(), (restaurante, value) -> restaurante.getUbicacion().setNombreProvincia(value));
        binder.forField(localidadCombobox).asRequired(getTranslation("error.campoobligatorio")).bind(restaurante -> restaurante.getUbicacion().getNombreLocalidad(), (restaurante, value) -> restaurante.getUbicacion().setNombreLocalidad(value));

        // Imagenes

        binder.readBean(restaurante);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.corroborarEstadoBar(event);
    }

}
