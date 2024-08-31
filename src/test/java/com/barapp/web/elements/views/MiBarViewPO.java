package com.barapp.web.elements.views;

import com.microsoft.playwright.Page;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class MiBarViewPO {
    @Getter(AccessLevel.NONE)
    private final Page page;

    public MiBarViewPO(Page page) {
        this.page = page;
    }
}
