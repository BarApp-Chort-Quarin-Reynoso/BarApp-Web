package com.barapp.web.views.components.pageElements;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class BarappFooter extends Footer {

    Span home = new Span(getTranslation("components.footer.home"));
    Span sobreNosotros = new Span(getTranslation("components.footer.sobrenosotros"));
    Span terminosCondiciones = new Span(getTranslation("components.footer.terminoscondiciones"));
    Span nuestroEquipo = new Span(getTranslation("components.footer.nuestroequipo"));
    Span blog = new Span(getTranslation("components.footer.blog"));
    Span ayuda = new Span(getTranslation("components.footer.ayuda"));
    Span copyright = new Span(getTranslation("components.footer.copyright"));

    public BarappFooter() {
        this.setClassName("footer");

        home.setClassName("footer-item");
        sobreNosotros.setClassName("footer-item");
        terminosCondiciones.setClassName("footer-item");
        nuestroEquipo.setClassName("footer-item");
        blog.setClassName("footer-item");
        ayuda.setClassName("footer-item");
        copyright.setClassName("footer-copy");

        Button facebook = new Button(LineAwesomeIcon.FACEBOOK.create());
        facebook.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button instagram = new Button(LineAwesomeIcon.INSTAGRAM.create());
        instagram.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button twitter = new Button(LineAwesomeIcon.TWITTER.create());
        twitter.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button whatsapp = new Button(LineAwesomeIcon.WHATSAPP.create());
        whatsapp.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout horizontalLayout = new HorizontalLayout(home, sobreNosotros, terminosCondiciones, nuestroEquipo, blog,ayuda);
        horizontalLayout.setClassName("items-list");
        HorizontalLayout container = new HorizontalLayout(horizontalLayout,new HorizontalLayout(facebook, instagram, twitter, whatsapp));
        container.setClassName("footer-layout-container");

        this.add(container,copyright);
    }
}
