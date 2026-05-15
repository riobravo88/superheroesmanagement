package com.example.application.views.heros;

import com.example.application.data.HeroProfile;
import com.example.application.data.Superhero;
import com.example.application.services.HeroProfileService;
import com.example.application.services.SuperheroService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.example.application.data.Publishers;
import com.example.application.services.PublishersService;
import java.util.List;
import java.util.Optional;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.example.application.data.Powers;
import jakarta.annotation.security.PermitAll;

@PageTitle("Heros")
@Route("heros/:superheroID?/:action?(edit)")
@Menu(order = 1, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@PermitAll
public class HerosView extends Div implements BeforeEnterObserver {

    private final String SUPERHERO_ID = "superheroID";
    private final String SUPERHERO_EDIT_ROUTE_TEMPLATE = "heros/%s/edit";

    private final Grid<Superhero> grid = new Grid<>(Superhero.class, false);

    private TextField name;
    private TextField spname;
    private TextField occupation;
    private TextField homeCity;
    private TextField universe;
    private TextField firstApperiance;

    private ComboBox<HeroProfile> heroProfile;
    private ComboBox<Publishers> publisher;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button addNew = new Button("Add New");

    private final BeanValidationBinder<Superhero> binder;

    private Superhero superhero;

    private final SuperheroService superheroService;
    private final HeroProfileService heroProfileService;
    private final PublishersService publishersService;

    public HerosView(SuperheroService superheroService,
                     HeroProfileService heroProfileService,
                     PublishersService publishersService) {

        this.superheroService = superheroService;
        this.heroProfileService = heroProfileService;
        this.publishersService = publishersService;

        addClassNames("heros-view");

        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);


        grid.addColumn("name");
        grid.addColumn("spname");
        grid.addColumn("occupation");
        grid.addColumn("homeCity");
        grid.addColumn("universe");
        grid.addColumn("firstApperiance");

        grid.addColumn(hero -> {
            HeroProfile profile = hero.getHeroProfile();
            return profile == null ? "-" : "Weakness: " + profile.getWeakness();
        }).setHeader("Hero Profile");

        grid.addColumn(hero -> {
            Publishers publisher = hero.getPublisher();
            return publisher == null ? "-" : publisher.getPublisherName();
        }).setHeader("Publisher");

        grid.addColumn(hero -> {

            if (hero.getPowers() == null || hero.getPowers().isEmpty()) {
                return "-";
            }

            return hero.getPowers().stream()
                    .map(Powers::getPowerName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");

        }).setHeader("Powers");

        grid.setItems(query ->
                superheroService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()
        );

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(
                        String.format(SUPERHERO_EDIT_ROUTE_TEMPLATE, event.getValue().getId())
                );
            } else {
                clearForm();
                UI.getCurrent().navigate(HerosView.class);
            }
        });
        

        binder = new BeanValidationBinder<>(Superhero.class);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (superhero == null) {
                    superhero = new Superhero();
                }

                binder.writeBean(superhero);

                superhero.setHeroProfile(heroProfile.getValue());
                superhero.setPublisher(publisher.getValue());

                superheroService.save(superhero);

                clearForm();
                refreshGrid();

                UI.getCurrent().navigate(HerosView.class);

                Notification.show("Saved");

            } catch (ValidationException ignored) {
                Notification.show("Validation error");
            }
        });



        delete.addClickListener(e -> {
            if (superhero != null) {
                superheroService.delete(superhero.getId());
                clearForm();
                refreshGrid();
                Notification.show("Deleted");
            }
        });

        addNew.addClickListener(e -> {
            superhero = new Superhero();
            binder.readBean(superhero);
            grid.select(null);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> id = event.getRouteParameters()
                .get(SUPERHERO_ID)
                .map(Long::parseLong);

        if (id.isPresent()) {
            superheroService.get(id.get()).ifPresentOrElse(
                    this::populateForm,
                    () -> {
                        Notification.show("Not found");
                        refreshGrid();
                        event.forwardTo(HerosView.class);
                    }
            );
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        FormLayout formLayout = new FormLayout();

        name = new TextField("Name");
        spname = new TextField("Spname");
        occupation = new TextField("Occupation");
        homeCity = new TextField("Home City");
        universe = new TextField("Universe");
        firstApperiance = new TextField("First Apperiance");

        heroProfile = new ComboBox<>("Hero Profile");

        heroProfile.setItems(heroProfileService.listAll());

        heroProfile.setItemLabelGenerator(p ->
                p == null ? "" : "Weakness: " + p.getWeakness()
        );

        heroProfile.setClearButtonVisible(true);

        publisher = new ComboBox<>("Publisher");
        publisher.setItems(publishersService.listAll());
        publisher.setItemLabelGenerator(p ->
                p == null ? "" : p.getPublisherName()
        );
        publisher.setClearButtonVisible(true);

        formLayout.add(
                name, spname, occupation,
                homeCity, universe,
                firstApperiance,
                heroProfile,
                publisher
        );

        editorLayoutDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addNew.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        buttonLayout.add(save, cancel, addNew, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        wrapper.add(grid);
        splitLayout.addToPrimary(wrapper);
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Superhero value) {
        superhero = value;
        binder.readBean(value);
        if (value != null) {
            heroProfile.setValue(value.getHeroProfile());
        } else {
            heroProfile.clear();
        }
        if (value != null) {
            heroProfile.setValue(value.getHeroProfile());
            publisher.setValue(value.getPublisher());
        } else {
            heroProfile.clear();
            publisher.clear();
        }
    }
}