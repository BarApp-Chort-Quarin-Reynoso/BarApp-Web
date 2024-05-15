package com.barapp.web.views.utils.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainElement extends VerticalLayout {
    public MainElement() {
        this.addClassName("main-element");
    }

    public MainElement(Component... children) {
        this();
        this.add(children);
    }
}
