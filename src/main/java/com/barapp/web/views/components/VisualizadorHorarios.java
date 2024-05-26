package com.barapp.web.views.components;

import com.barapp.web.model.Horario;
import com.barapp.web.model.enums.TipoComida;
import com.barapp.web.utils.FormatUtils;
import com.flowingcode.vaadin.addons.badgelist.Badge;
import com.flowingcode.vaadin.addons.badgelist.BadgeList;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisualizadorHorarios extends CustomField<List<Horario>> {

    VerticalLayout layout;

    Span emptyText = new Span(getTranslation("comp.visualizadorhorarios.emptytext"));

    Map<TipoComida, List<String>> horarios = new LinkedHashMap<>();

    public VisualizadorHorarios() {
        this.setWidthFull();
        layout = new VerticalLayout();
        layout.add(emptyText);

        add(layout);
    }

    @Override
    protected List<Horario> generateModelValue() {
        return new ArrayList<>();
    }

    @Override
    protected void setPresentationValue(List<Horario> newValue) {
        layout.removeAll();
        horarios.clear();

        if (newValue.isEmpty()) {
            layout.add(emptyText);
            return;
        }

        for (Horario h : newValue) {
            horarios.computeIfAbsent(h.getTipoComida(), k -> new ArrayList<>());
            horarios.get(h.getTipoComida()).add(h.getHorario().format(FormatUtils.timeFormatter()));
        }

        for (Map.Entry<TipoComida, List<String>> e : horarios.entrySet()) {
            if (!e.getValue().isEmpty()) {
                H4 tipoComidaTitle = new H4(getTranslation(e.getKey().getTranslationKey()));
                tipoComidaTitle.addClassName(LumoUtility.FontSize.MEDIUM);

                List<Badge> badges = new ArrayList<>();
                e.getValue().forEach(h -> {
                    Badge badge = new Badge(h);
                    badge.addThemeName("contrast");
                    badges.add(badge);
                });
                BadgeList badgesHorarios = new BadgeList(badges);
                badgesHorarios.setWidthFull();
                badgesHorarios.addThemeName("contrast");

                layout.add(tipoComidaTitle, badgesHorarios);
            }
        }
    }
}
