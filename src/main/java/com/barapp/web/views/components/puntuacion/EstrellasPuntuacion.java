package com.barapp.web.views.components.puntuacion;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;
import java.util.List;

public class EstrellasPuntuacion extends CustomField<Double> {
    private List<Estrella> estrellas = new ArrayList<>();

    private HorizontalLayout layout;

    private Span sinOpinionesLabel;

    public EstrellasPuntuacion() {
        for (int i = 0; i < 5; i++) {
            estrellas.add(new Estrella());
        }

        sinOpinionesLabel = new Span();
        sinOpinionesLabel.addClassNames(LumoUtility.FontSize.SMALL);
        sinOpinionesLabel.getStyle().set("font-style", "italic");

        layout = new HorizontalLayout();

        layout.getThemeList().clear();
        layout.addClassNames(LumoUtility.Gap.XSMALL);
        this.add(layout);
        this.addClassNames("estrellas-puntuacion");
        actualizarEstrellas(0.0);
    }

    @Override
    protected Double generateModelValue() {
        return estrellas.stream().map(Estrella::getPuntuacion).reduce(0.0, Double::sum);
    }

    @Override
    protected void setPresentationValue(Double newPresentationValue) {
        actualizarEstrellas(newPresentationValue);
    }

    private void actualizarEstrellas(Double puntuacion) {
        for (int i = 0; i < 5; i++) {
            if (i + 1 <= puntuacion) {
                estrellas.get(i).setPuntuacion(1.0);
            } else if (puntuacion - i < 1) {
                estrellas.get(i).setPuntuacion(puntuacion - i);
            } else {
                estrellas.get(i).setPuntuacion(0.0);
            }
        }

        layout.removeAll();
        estrellas.forEach(est -> layout.add(est.getIcon()));
    }

    public void setSinOpiniones(boolean sinOpiniones) {
        if (sinOpiniones) {
            sinOpinionesLabel.setText(getTranslation("views.opiniones.cantidadopiniones.sinopiniones"));
            this.remove(layout);
            this.add(sinOpinionesLabel);
        } else {
            this.remove(sinOpinionesLabel);
            this.add(layout);
        }

        layout.setVisible(!sinOpiniones);
    }
}
