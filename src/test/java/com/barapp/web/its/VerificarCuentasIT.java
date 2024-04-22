package com.barapp.web.its;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.barapp.web.elements.views.HomeViewPO;
import com.barapp.web.elements.views.ListaBaresViewPO;
import com.barapp.web.elements.views.LoginViewPO;
import com.barapp.web.utils.TestConsts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barapp.web.BaseIT;
import com.barapp.web.model.EstadoRestaurante;

import java.util.HashSet;
import java.util.Set;

public class VerificarCuentasIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();

    protected VerificarCuentasIT() {
        super();
    }
    @Override
    @BeforeEach
    protected void beforeEach() {
        super.beforeEach();
    }

    @AfterEach
    protected void afterEach() {
        usersCreated.forEach(correo -> eliminarUsuarioBar(correo));
        usersCreated.clear();
    }

    @Test
    @DisplayName("Probar a aceptar una cuenta")
    public void testAceptarCuenta() {
        String correo = crearBarConEstado("esperandohabilitacion", EstadoRestaurante.ESPERANDO_HABILITACION);
        usersCreated.add(correo);

        HomeViewPO homeView = goToHomeView();
        homeView.getLoginButton().click();

        LoginViewPO loginView = new LoginViewPO(page);
        loginView.login(correo, TestConsts.PASSWORD_TEST);

        assertThat(page).hasURL(rootUrl + "bar-en-espera-habilitacion");

        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();

        loginView.login(TestConsts.CORREO_ADMIN, TestConsts.PASSWORD_TEST);

        assertThat(homeView.getListaBaresTab()).isVisible();
        homeView.getListaBaresTab().click();

        ListaBaresViewPO baresViewPO = new ListaBaresViewPO(page);
        assertThat(baresViewPO.getEstadoBarLocator(correo)).containsText("Esperando habilitación");

        baresViewPO.aceptarBar(correo);
        assertThat(baresViewPO.getEstadoBarLocator(correo)).containsText("Habilitado");

        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();

        loginView.login(correo, TestConsts.PASSWORD_TEST);
        assertThat(page).not().hasURL(rootUrl + "bar-en-espera-habilitacion");
    }

    @Test
    @DisplayName("Probar a rechazar una cuenta")
    public void testRechzarCuenta() {
        String correo = crearBarConEstado("rechazar", EstadoRestaurante.ESPERANDO_HABILITACION);
        usersCreated.add(correo);

        HomeViewPO homeView = goToHomeView();
        homeView.getLoginButton().click();

        LoginViewPO loginView = new LoginViewPO(page);
        loginView.login(correo, TestConsts.PASSWORD_TEST);

        assertThat(page).hasURL(rootUrl + "bar-en-espera-habilitacion");

        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();

        loginView.login(TestConsts.CORREO_ADMIN, TestConsts.PASSWORD_TEST);

        assertThat(homeView.getListaBaresTab()).isVisible();
        homeView.getListaBaresTab().click();

        ListaBaresViewPO baresViewPO = new ListaBaresViewPO(page);
        assertThat(baresViewPO.getEstadoBarLocator(correo)).containsText("Esperando habilitación");

        baresViewPO.rechazarBar(correo);
        assertThat(baresViewPO.getEstadoBarLocator(correo)).containsText("Rechazado");

        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();

        loginView.login(correo, TestConsts.PASSWORD_TEST);
        assertThat(page).hasURL(rootUrl + "bar-no-habilitado");
    }
}
