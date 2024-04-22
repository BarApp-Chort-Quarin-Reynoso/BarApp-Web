package com.barapp.web.its;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.barapp.web.model.EstadoRestaurante;
import com.barapp.web.utils.TestConsts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barapp.web.BaseIT;
import com.barapp.web.elements.views.HomeViewPO;
import com.barapp.web.elements.views.LoginViewPO;

import java.util.HashSet;
import java.util.Set;

public class LoginIT extends BaseIT {
    private Set<String> usersCreated = new HashSet<>();
    
    protected LoginIT() {
        super();
    }

    @AfterEach
    protected void afterEach() {
        usersCreated.forEach(correo -> eliminarUsuarioBar(correo));
        usersCreated.clear();
    }
    
    @Test
    @DisplayName("Probar login y roles")
    public void testLogin() {
        String correo = crearBarConEstado("testbar", EstadoRestaurante.HABILITADO);
        usersCreated.add(correo);

        HomeViewPO homeView = goToHomeView();

        assertThat(homeView.getMiBarTab()).not().isVisible();

        homeView.getLoginButton().click();
        
        LoginViewPO loginView = new LoginViewPO(page);
        loginView.login("correoincorrecto@gmail.com", "incorrecta");
        
        assertThat(loginView.getErrorDiv()).isVisible();

        loginView.login(correo, "incorrecta");
        
        assertThat(loginView.getErrorDiv()).isVisible();

        loginView.login(correo, TestConsts.PASSWORD_TEST);
        
        assertThat(homeView.getMiBarTab()).isVisible();
        
        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();

        loginView.login(TestConsts.CORREO_ADMIN, TestConsts.PASSWORD_TEST);

        assertThat(loginView.getErrorDiv()).not().isVisible();
        assertThat(homeView.getMiBarTab()).not().isVisible();
    }
}
