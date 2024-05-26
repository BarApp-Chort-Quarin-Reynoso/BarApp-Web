package com.barapp.web.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UiUtils {
    public static Component setTopLabelToComponent(Component component, String label) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-xs");
        layout.setWidth("unset");

        Span spanLabel = new Span(label);
        spanLabel.addClassName("vaadin-like-label");

        layout.add(spanLabel, component);

        return layout;
    }
}
