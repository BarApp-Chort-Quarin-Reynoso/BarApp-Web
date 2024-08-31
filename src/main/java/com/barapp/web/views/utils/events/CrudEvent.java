package com.barapp.web.views.utils.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

@Getter
public abstract class CrudEvent<T extends Component, O> extends ComponentEvent<T> {
    private final O bean;

    public CrudEvent(T source, boolean fromClient, O bean) {
        super(source, fromClient);
        this.bean = bean;
    }
}
