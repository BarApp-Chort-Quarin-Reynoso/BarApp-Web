package com.barapp.web.views;

import com.barapp.web.business.service.UsuarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@PageTitle("Mi Bar")
@Route(value = "mi-bar", layout = MainLayout.class)
public class MiBarView extends VerticalLayout {

    private final UsuarioService usuarioService;

    H3 tituloH3;
    TextField nombreTextfield;
    TextField direccionTextfield;
    TextField menuTextfield;
    Button cargarMenuButton;
    Button gestionarCapacidadButton;
    Button guardarButton;
    Button cancelarButton;
    Button cargarUsuarios;

    FormLayout formLayout;

    public MiBarView(UsuarioService usuarioService) {
	this.usuarioService = usuarioService;

	configurarElementos();
	configurarLayout();
	configurarBinders();
    }

    private void configurarElementos() {
	tituloH3 = new H3(getTranslation("views.mibar.titulo"));

	nombreTextfield = new TextField(getTranslation("views.mibar.nombre"));

	direccionTextfield = new TextField(getTranslation("views.mibar.direccion"));

	menuTextfield = new TextField(getTranslation("views.mibar.menu"));
	menuTextfield.setWidthFull();

	cargarMenuButton = new Button(getTranslation("views.mibar.cargarmenu"));

	gestionarCapacidadButton = new Button(getTranslation("views.mibar.gestionarcapacidad"));

	guardarButton = new Button(getTranslation("commons.save"));
	guardarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

	cancelarButton = new Button(getTranslation("commons.cancel"));
	cargarUsuarios = new Button(getTranslation("Cargar usuarios"));
    }

    private void configurarLayout() {
	formLayout = new FormLayout();

	HorizontalLayout layoutCargarMenu = new HorizontalLayout(menuTextfield, cargarMenuButton);
	layoutCargarMenu.setAlignItems(Alignment.END);
	formLayout.add(nombreTextfield, direccionTextfield, layoutCargarMenu, gestionarCapacidadButton);
	formLayout.setResponsiveSteps(
		// Use one column by default
		new ResponsiveStep("0", 1),
		// Use two columns, if layout's width exceeds 500px
		new ResponsiveStep("500px", 2));

	HorizontalLayout layoutBotones = new HorizontalLayout(cancelarButton, guardarButton, cargarUsuarios);

	this.add(tituloH3, formLayout, layoutBotones);
    }

    private void configurarBinders() {
    }
}
