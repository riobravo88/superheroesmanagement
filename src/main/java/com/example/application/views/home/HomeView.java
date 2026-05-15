package com.example.application.views.home;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.button.Button;

@PageTitle("Home")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.FILE)
@AnonymousAllowed
public class HomeView extends VerticalLayout {

    public HomeView() {
        addClassName("home-view");
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("text-align", "center");

        H1 title = new H1("Superheroes Management");

        Paragraph description = new Paragraph(
                "This application is used to manage superheroes, profiles, publishers and powers."
        );

        UnorderedList features = new UnorderedList(
                new ListItem("CRUD operations for all main entities"),
                new ListItem("Relations between superheroes, profiles, publishers and powers"),
                new ListItem("Advanced search and authentication")
        );


        add(title, description, features);
    }

}
