package com.barapp.web.views.components;

import com.barapp.web.model.Opinion;
import com.barapp.web.utils.FormatUtils;
import com.barapp.web.views.components.puntuacion.EstrellasPuntuacion;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class VisualizadorOpinion extends VerticalLayout {
    private Avatar avatar;
    private Span usuario;
    private Span fecha;
    private EstrellasPuntuacion puntuacion;
    private Paragraph comentario;

    public VisualizadorOpinion() {
        avatar = new Avatar();
        usuario = new Span();
        fecha = new Span();
        puntuacion = new EstrellasPuntuacion();
        comentario = new Paragraph();

        fecha.addClassName("opinion-fecha");
        fecha.getStyle().setMarginLeft("auto");

        HorizontalLayout usuarioLayout = new HorizontalLayout();
        usuarioLayout.setSpacing(false);
        usuarioLayout.addClassNames(LumoUtility.Gap.SMALL);
        usuarioLayout.setAlignItems(Alignment.CENTER);
        usuarioLayout.setWidthFull();
        usuarioLayout.add(avatar, usuario, fecha);

        add(usuarioLayout, puntuacion, comentario);
    }

    public VisualizadorOpinion(Opinion opinion) {
        this();

        setValue(opinion);
    }

    public void setValue(Opinion opinion) {
        avatar.setImage(opinion.getUsuario().getFoto());
        usuario.add(opinion.getUsuario().getNombre() + " " + opinion.getUsuario().getApellido());
        fecha.setText(opinion.getFecha().format(FormatUtils.visualizationDateFormatter()));
        puntuacion.setValue(opinion.getNota());
        comentario.setText(opinion.getComentario());
    }
}
