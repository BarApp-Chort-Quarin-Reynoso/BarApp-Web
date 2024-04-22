package com.barapp.web.elements.views;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class HomeViewPO {
    @Getter(AccessLevel.NONE)
    private final Page page;
    
    private final Locator miBarTab;
    private final Locator listaBaresTab;
    private final Locator loginButton;
    private final Locator logoutButton;
    
    public HomeViewPO(Page page) {
        this.page = page;
        miBarTab = page.getByText("Mi Bar");
        listaBaresTab = page.getByText("Bares");
        loginButton = page.locator("#login-button");
        logoutButton = page.locator("#logout-button");
    }
}
