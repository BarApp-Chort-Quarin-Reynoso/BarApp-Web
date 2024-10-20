package com.barapp.web.elements.views;

import com.barapp.web.elements.BaseViewPO;
import com.microsoft.playwright.Page;

public class RegistrarFormPO extends BaseViewPO {
    public RegistrarFormPO(Page page) {
        super(page, "registro");
    }
}
