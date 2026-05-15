package com.example.application.views.search;

import com.example.application.data.Publishers;
import com.example.application.data.Superhero;
import com.example.application.services.PublishersService;
import com.example.application.services.SuperheroSearchService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import jakarta.annotation.security.PermitAll;

@PageTitle("Search")
@Route("search")
@Menu(order = 5, icon = LineAwesomeIconUrl.FILE)
@PermitAll
public class SearchView extends VerticalLayout {

    private final TextField keyword = new TextField("Search name / superhero name");
    private final TextField universe = new TextField("Universe");
    private final ComboBox<Publishers> publisher = new ComboBox<>("Publisher");

    private final Button search = new Button("Search");
    private final Button clear = new Button("Clear");

    private final Grid<Superhero> grid = new Grid<>(Superhero.class, false);

    public SearchView(SuperheroSearchService searchService,
                      PublishersService publishersService) {

        addClassName("search-view");
        setSizeFull();

        publisher.setItems(publishersService.listAll());
        publisher.setItemLabelGenerator(Publishers::getPublisherName);
        publisher.setClearButtonVisible(true);

        grid.addColumn("name").setHeader("Name");
        grid.addColumn("spname").setHeader("Hero name");
        grid.addColumn("occupation").setHeader("Occupation");
        grid.addColumn("homeCity").setHeader("Home city");
        grid.addColumn("universe").setHeader("Universe");

        grid.addColumn(hero -> {
            if (hero.getPublisher() == null) {
                return "-";
            }
            return hero.getPublisher().getPublisherName();
        }).setHeader("Publisher");

        search.addClickListener(e -> {
            grid.setItems(searchService.search(
                    keyword.getValue(),
                    universe.getValue(),
                    publisher.getValue()
            ));
        });

        clear.addClickListener(e -> {
            keyword.clear();
            universe.clear();
            publisher.clear();
            grid.setItems(searchService.search(null, null, null));
        });

        HorizontalLayout filters = new HorizontalLayout(keyword, universe, publisher, search, clear);
        filters.setAlignItems(Alignment.END);

        add(filters, grid);

        grid.setSizeFull();
        expand(grid);

        grid.setItems(searchService.search(null, null, null));
    }
}