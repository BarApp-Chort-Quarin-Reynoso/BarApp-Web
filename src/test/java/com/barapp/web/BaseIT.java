package com.barapp.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.barapp.web.elements.views.HomeViewPO;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import in.virit.mopo.Mopo;

public abstract class BaseIT {
    protected Page page;
    private static String rootUrl;
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;

    @BeforeAll
    protected static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(400));
        rootUrl = "http://localhost:8080";
    }

    @BeforeEach
    protected void createContextAndPage() {
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
}
