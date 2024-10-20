package com.barapp.web.elements.views;

import com.barapp.web.elements.BaseViewPO;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class MiBarViewPO extends BaseViewPO {
    @Getter(AccessLevel.NONE)
    private final Page page;

    public MiBarViewPO(Page page) {
        super(page, "mi-bar");
        this.page = page;
    }

    public Locator getFormTextInput(String fieldName) {
        return page
                .locator("vaadin-text-field")
                .filter(new Locator.FilterOptions().setHasText(fieldName))
                .locator("input");
    }

    public Locator getFormTextInputErrorLabel(String fieldName) {
        return page
                .locator("vaadin-text-field")
                .filter(new Locator.FilterOptions().setHasText(fieldName))
                .locator("[slot='error-message']");
    }

    public Locator getFormIntegerInput(String fieldName) {
        return page
                .locator("vaadin-integer-field")
                .filter(new Locator.FilterOptions().setHasText(fieldName))
                .locator("input");
    }

    public Locator getFormIntegerInputErrorLabel(String fieldName) {
        return page
                .locator("vaadin-integer-field")
                .filter(new Locator.FilterOptions().setHasText(fieldName))
                .locator("[slot='error-message']");
    }
}
