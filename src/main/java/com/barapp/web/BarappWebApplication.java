package com.barapp.web;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("serial")
@SpringBootApplication
@Theme(value = "barapp-web")
@NpmPackage(value = "@fontsource/nunito-sans", version = "4.5.0")
public class BarappWebApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(BarappWebApplication.class, args);
	}

}
