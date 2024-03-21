package com.barapp.web;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("serial")
@SpringBootApplication
@Theme(value = "barapp-web")
@NpmPackage(value = "@fontsource/sansita-swashed", version = "5.0.17")
public class BarappWebApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(BarappWebApplication.class, args);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        AppShellConfigurator.super.configurePage(settings);
        //      settings.addFavIcon("icon", "icons/icon.png", "162x162");
        //      settings.addLink("shortcut icon", "icons/favicon.ico");
    }
}
