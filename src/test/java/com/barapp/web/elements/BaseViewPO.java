package com.barapp.web.elements;

import com.microsoft.playwright.Page;

public class BaseViewPO {
    protected Page page;
    protected String url;
    protected static String rootUrl = "http://localhost:8080/";

    public BaseViewPO(Page page, String resource) {
        this.page = page;
        this.url = rootUrl + resource;
    }

    public void navigate() {
        page.navigate(url);
    }

    public void clickGuardar() {
        page.getByText("Guardar").click();
    }

    public void clickCancelar() {
        page.getByText("Cancelar").click();
    }
}
