package com.barapp.web.its;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barapp.web.BaseIT;
import com.barapp.web.elements.views.HomeViewPO;
import com.barapp.web.elements.views.LoginViewPO;

public class LoginIT extends BaseIT {
    
    protected LoginIT() {
        super();
    }
    
    @Test
    @DisplayName("Probar login y roles")
    public void testLogin() {
        HomeViewPO homeView = goToHomeView();
        homeView.getLoginButton().click();
        
        LoginViewPO loginView = new LoginViewPO(page);
        loginView.getEmailField().fill("correoinexistente@gmail.com");
        loginView.getPasswordField().fill("incorrecta");
        loginView.getLoginButton().click();
        
        assertThat(loginView.getErrorDiv()).isVisible();
        
        loginView.getEmailField().fill("bar@gmail.com");
        loginView.getPasswordField().fill("incorrecta");
        loginView.getLoginButton().click();
        
        assertThat(loginView.getErrorDiv()).isVisible();
        
        loginView.getEmailField().fill("bar@gmail.com");
        loginView.getPasswordField().fill("password");
        loginView.getLoginButton().click();
        
        assertThat(homeView.getMiBarTab()).isVisible();
        
        homeView.getLogoutButton().click();
        homeView.getLoginButton().click();
        
        loginView.getEmailField().fill("admin@gmail.com");
        loginView.getPasswordField().fill("password");
        loginView.getLoginButton().click();
        
        assertThat(homeView.getMiBarTab()).not().isVisible();        
    }
}
