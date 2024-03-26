package com.barapp.web.elements.views;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import lombok.Getter;
import lombok.AccessLevel;

@Getter
public class LoginViewPO {
    @Getter(AccessLevel.NONE)
    private final Page page;
    
    private final Locator emailField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator errorDiv;
    
    public LoginViewPO(Page page) {
        this.page = page;
        this.emailField = page.locator("vaadin-text-field > input");
        this.passwordField = page.locator("vaadin-password-field > input");
        this.loginButton = page.locator("vaadin-button[slot='submit']");
        this.errorDiv = page.locator("section[part='form'] > div[part='error-message']");
    }
}
