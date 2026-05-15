package com.example.application.views.publishers;

import com.example.application.data.Publishers;
import com.example.application.services.PublishersService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.example.application.data.Superhero;
import jakarta.annotation.security.PermitAll;

@PageTitle("Publishers")
@Route("publishers/:publishersID?/:action?(edit)")
@Menu(order = 4, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@PermitAll
public class PublishersView extends Div implements BeforeEnterObserver {

    private final String PUBLISHERS_ID = "publishersID";
    private final String PUBLISHERS_EDIT_ROUTE_TEMPLATE = "publishers/%s/edit";

    private final Grid<Publishers> grid = new Grid<>(Publishers.class, false);

    private TextField publisherName;
    private TextField country;
    private TextField foundedYear;
    private TextField ceo;
    private TextField universe;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button addNew = new Button("Add new");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<Publishers> binder;

    private Publishers publishers;

    private final PublishersService publishersService;

    public PublishersView(PublishersService publishersService) {
        this.publishersService = publishersService;
        addClassNames("publishers-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("publisherName").setAutoWidth(true);
        grid.addColumn("country").setAutoWidth(true);
        grid.addColumn("foundedYear").setAutoWidth(true);
        grid.addColumn("ceo").setAutoWidth(true);
        grid.addColumn("universe").setAutoWidth(true);
        grid.addColumn(publisher -> {
            if (publisher.getSuperheroes() == null || publisher.getSuperheroes().isEmpty()) {
                return "-";
            }

            return publisher.getSuperheroes().stream()
                    .map(Superhero::getSpname)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");
        }).setHeader("Heroes");

        grid.setItems(query -> publishersService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PUBLISHERS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PublishersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Publishers.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.publishers == null) {
                    this.publishers = new Publishers();
                }
                binder.writeBean(this.publishers);
                publishersService.save(this.publishers);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PublishersView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

        addNew.addClickListener(e -> {
            grid.asSingleSelect().clear();
            this.publishers = null;
            clearForm();
        });

        delete.addClickListener(e -> {
            if (this.publishers != null && this.publishers.getId() != null) {
                publishersService.delete(this.publishers.getId());
                Notification.show("Deleted publisher");

                this.publishers = null;
                clearForm();
                refreshGrid();
            } else {
                Notification.show("Select a publisher first");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> publishersId = event.getRouteParameters().get(PUBLISHERS_ID).map(Long::parseLong);
        if (publishersId.isPresent()) {
            Optional<Publishers> publishersFromBackend = publishersService.get(publishersId.get());
            if (publishersFromBackend.isPresent()) {
                populateForm(publishersFromBackend.get());
            } else {
                Notification.show(String.format("The requested publishers was not found, ID = %s", publishersId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PublishersView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        publisherName = new TextField("Publisher Name");
        country = new TextField("Country");
        foundedYear = new TextField("Founded Year");
        ceo = new TextField("Ceo");
        universe = new TextField("Universe");
        formLayout.add(publisherName, country, foundedYear, ceo, universe);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        addNew.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        buttonLayout.add(save, cancel, addNew, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Publishers value) {
        this.publishers = value;
        binder.readBean(this.publishers);

    }
}
