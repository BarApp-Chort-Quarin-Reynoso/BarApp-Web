package com.barapp.web.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class Footer extends VerticalLayout {

    Span home = new Span(getTranslation("components.footer.home"));
    Span sobreNosotros = new Span(getTranslation("components.footer.sobrenosotros"));
    Span terminosCondiciones = new Span(getTranslation("components.footer.terminoscondiciones"));
    Span nuestroEquipo = new Span(getTranslation("components.footer.nuestroequipo"));
    Span blog = new Span(getTranslation("components.footer.blog"));
    Span ayuda = new Span(getTranslation("components.footer.ayuda"));
    Span copyright = new Span(getTranslation("components.footer.copyright"));

    public Footer() {
        this.setClassName("footer");

        home.setClassName("footer-item");
        sobreNosotros.setClassName("footer-item");
        terminosCondiciones.setClassName("footer-item");
        nuestroEquipo.setClassName("footer-item");
        blog.setClassName("footer-item");
        ayuda.setClassName("footer-item");
        copyright.setClassName("footer-copy");

        Button facebook = new Button(LineAwesomeIcon.FACEBOOK.create());

        Button instagram = new Button(LineAwesomeIcon.INSTAGRAM.create());

        Button twitter = new Button(LineAwesomeIcon.TWITTER.create());

        Button whatsapp = new Button(LineAwesomeIcon.WHATSAPP.create());

        this.add(new HorizontalLayout(
                        home,
                        sobreNosotros,
                        terminosCondiciones,
                        nuestroEquipo,
                        blog,
                        ayuda),
                new HorizontalLayout(
                        facebook,
                        instagram,
                        twitter,
                        whatsapp
                ),
                copyright);
    }
}
