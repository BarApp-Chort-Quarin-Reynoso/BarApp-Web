package com.barapp.web.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainElement extends Main {
    public MainElement() {
        this.addClassName("main-element");
    }

    public MainElement(Component... children) {
        this();
        this.add(children);
    }
}
