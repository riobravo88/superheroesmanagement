package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.util.List;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    private final AuthenticationContext authenticationContext;
    private H1 viewTitle;

    public MainLayout(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Optional<UserDetails> user = authenticationContext.getAuthenticatedUser(UserDetails.class);

        Span userInfo = new Span(
                user.map(u -> "User: " + u.getUsername())
                        .orElse("Not logged in")
        );
        userInfo.getStyle().set("margin-right", "20px");
        userInfo.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Button logout = new Button(user.isPresent() ? "Logout" : "Login");

        logout.addClickListener(e -> {
            if (user.isPresent()) {
                authenticationContext.logout();
            } else {
                logout.getUI().ifPresent(ui -> ui.navigate("login"));
            }
        });

        Span spacer = new Span();
        spacer.getStyle().set("flex-grow", "1");

        addToNavbar(true, toggle, viewTitle, spacer, userInfo, logout);
    }

    private void addDrawerContent() {
        Span appName = new Span("SuperherosManagement");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });

        return nav;
    }

    private Footer createFooter() {
        Footer footer = new Footer();

        Span text = new Span("© 2026 Ossi Jyrkinen | Vaadin Web harjoitustyö");
        text.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        footer.add(text);
        footer.addClassNames(
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.Border.TOP
        );

        footer.getStyle().set("margin-top", "auto");
        footer.getStyle().set("background", "var(--lumo-contrast-5pct)");

        return footer;

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}
