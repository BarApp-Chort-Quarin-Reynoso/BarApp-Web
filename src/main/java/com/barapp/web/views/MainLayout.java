package com.barapp.web.views;

import com.barapp.web.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

@SuppressWarnings("serial")
public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        addToNavbar(createHeaderContent());
        setDrawerOpened(false);
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1(getTranslation("commons.apptitle"));
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.LARGE);
        layout.add(appName);

        Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);
        nav.getStyle().set("flex-grow", "1");

        UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
        nav.add(list);

        list.add(new MenuItemInfo(getTranslation("views.inicio.titulo"), LineAwesomeIcon.HOME_SOLID.create(),
                InicioView.class));

        HorizontalLayout navWrapper = new HorizontalLayout();
        navWrapper.getStyle().setPadding("var(--lumo-space-s) var(--lumo-space-m)");
        navWrapper.setWidthFull();
        navWrapper.add(nav);
        navWrapper.setBoxSizing(com.vaadin.flow.component.orderedlayout.BoxSizing.BORDER_BOX);

        if (securityService.isAuthenticated()) {
            for (MenuItemInfo menuItem : createMenuItemsForLoggedInUsers()) {
                list.add(menuItem);
            }

            Button logoutButton = new Button(getTranslation("commons.logout"));
            logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            logoutButton.addClickListener(e -> securityService.logout());
            navWrapper.add(logoutButton);
        } else {
            Button loginButton = new Button(getTranslation("commons.login"));
            loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            loginButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
            navWrapper.add(loginButton);
        }

        header.add(layout, navWrapper);

        return header;
    }

    private MenuItemInfo[] createMenuItemsForLoggedInUsers() {
        UserDetails user = securityService.getAuthenticatedUser().get();
        List<MenuItemInfo> items = new ArrayList<>();
        if (user.getAuthorities()
                .contains(new SimpleGrantedAuthority(MiBarView.rolAllowed.getGrantedAuthorityName()))) {
            items.add(new MenuItemInfo(getTranslation("views.mibar.titulo"), LineAwesomeIcon.UTENSILS_SOLID.create(),
                    MiBarView.class));
        }
        return items.toArray(new MenuItemInfo[] {});
    }

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }
}
