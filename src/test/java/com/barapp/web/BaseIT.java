package com.barapp.web;

import com.barapp.web.elements.views.HomeViewPO;
import com.barapp.web.model.enums.EstadoRestaurante;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseIT {
    protected Page page;
    protected static String rootUrl;
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;

    @BeforeAll
    protected static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(400));
        rootUrl = "http://localhost:8080/";
    }

    @BeforeEach
    protected void beforeEach() {
        context = browser.newContext(new Browser.NewContextOptions());
        page = context.newPage();

        page.navigate(rootUrl);
    }

    @AfterEach
    protected void closeContext() {
        context.close();
    }

    @AfterAll
    protected static void closeBrowser() {
        playwright.close();
    }

    protected HomeViewPO goToHomeView() {
        page.navigate(rootUrl);

        return new HomeViewPO(page);
    }

    protected String crearBarConEstado(String username, EstadoRestaurante estado) {
        String estadoString = estado.toString();
        String route = rootUrl + "fakeview/crearUsuarioBarConEstado?params=" + username
                + "&params=" + estadoString;
        page.navigate(route);
        page.waitForURL(route);

        return page.locator("#return-span").textContent();
    }

    protected void eliminarUsuarioBar(String correo) {
        String route = rootUrl + "fakeview/eliminarUsuarioBar?params=" + correo;
        page.navigate(route);
        page.waitForURL(route);
    }
}
