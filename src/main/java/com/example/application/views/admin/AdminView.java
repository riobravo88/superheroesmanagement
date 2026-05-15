package com.example.application.views.admin;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admin")
@Route("admin")
@Menu(order = 6, icon = LineAwesomeIconUrl.FILE)
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {

    public AdminView() {
        addClassName("admin-view");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Admin Dashboard");

        Paragraph info = new Paragraph(
                "This page is only visible for users with ADMIN role."
        );

        Div usersCard = createCard("Users", "Admin can manage users and roles here.");
        Div securityCard = createCard("Security", "Authentication and authorization are enabled.");
        Div systemCard = createCard("System", "Application is running with Vaadin and Spring Boot.");

        HorizontalLayout cards = new HorizontalLayout(usersCard, securityCard, systemCard);
        cards.setWidthFull();

        add(title, info, cards);
    }

    private Div createCard(String title, String text) {
        Div card = new Div();
        card.addClassName("admin-card");
        card.getStyle().set("padding", "var(--lumo-space-m)");
        card.getStyle().set("border-radius", "var(--lumo-border-radius-l)");
        card.getStyle().set("box-shadow", "0 4px 12px rgba(0,0,0,0.12)");
        card.getStyle().set("background", "var(--lumo-base-color)");
        card.getStyle().set("width", "250px");

        H1 cardTitle = new H1(title);
        Paragraph cardText = new Paragraph(text);

        card.add(cardTitle, cardText);
        return card;
    }

}
