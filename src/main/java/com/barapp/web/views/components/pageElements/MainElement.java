package com.barapp.web.views.components.pageElements;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Main;

public class MainElement extends Main {
    public MainElement() {
        this.addClassName("main-element");
    }

    public MainElement(Component... children) {
        this();
        this.add(children);
    }
}
