package com.barapp.web.views.dialogs;

import com.barapp.web.model.Opinion;
import com.barapp.web.views.components.VisualizadorOpinion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpinionesDialog extends Dialog {
    final List<Opinion> opiniones;

    final Button closeButton;
    final VirtualList<Opinion> listaOpiniones;

    public OpinionesDialog(List<Opinion> opiniones) {
        this.opiniones = opiniones;

        closeButton = new Button(LineAwesomeIcon.TIMES_SOLID.create(), e -> this.close());
        listaOpiniones = new VirtualList<>();

        configureUI();
    }

    private void configureUI() {
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        listaOpiniones.setHeightFull();
        listaOpiniones.setWidthFull();
        listaOpiniones.setItems(opiniones);
        listaOpiniones.setRenderer(new ComponentRenderer<>(item -> new VisualizadorOpinion(item)));

        setHeaderTitle(getTranslation("comp.opinionesdialog.titulo"));
        getHeader().add(closeButton);

        VerticalLayout contentWrapper = new VerticalLayout();
        contentWrapper.setSizeFull();
        contentWrapper.setPadding(false);
        contentWrapper.addClassNames(LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Bottom.MEDIUM);
        contentWrapper.add(listaOpiniones);
        add(contentWrapper);

        setWidth("50%");
        setHeight("80%");
        setMinWidth("400px");
    }
}
