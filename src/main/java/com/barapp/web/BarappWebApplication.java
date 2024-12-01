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

        settings.addFavIcon("icon",
                "https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Flogos%2Ffavicon.png?alt=media&token=f30e457f-f86d-4845-b28e-439fbd7c502a",
                "162x162");
        settings.addLink("shortcut icon",
                "https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Flogos%2Ffavicon.png?alt=media&token=f30e457f-f86d-4845-b28e-439fbd7c502a");

        settings.addMetaTag("og:title", "Barapp");
        settings.addMetaTag("og:description", "¡Reservá en tu restaurante favorito hoy!");
        settings.addMetaTag("og:image",
                "https://firebasestorage.googleapis.com/v0/b/barapp-b1bc0.appspot.com/o/images%2Flogos%2Ffavicon.png?alt=media&token=f30e457f-f86d-4845-b28e-439fbd7c502a");
        settings.addMetaTag("og:url", "https://barapp.click");
        settings.addMetaTag("og:type", "website");
    }
}
