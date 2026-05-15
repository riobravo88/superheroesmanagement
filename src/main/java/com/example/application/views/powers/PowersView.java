package com.example.application.views.powers;

import com.example.application.data.Powers;
import com.example.application.services.PowersService;
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
import jakarta.annotation.security.PermitAll;

@PageTitle("Powers")
@Route("powers/:powersID?/:action?(edit)")
@Menu(order = 3, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@PermitAll
public class PowersView extends Div implements BeforeEnterObserver {

    private final String POWERS_ID = "powersID";
    private final String POWERS_EDIT_ROUTE_TEMPLATE = "powers/%s/edit";

    private final Grid<Powers> grid = new Grid<>(Powers.class, false);

    private TextField powerName;
    private TextField description;
    private TextField type;
    private TextField strenghtLvl;
    private TextField isRare;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button addNew = new Button("Add new");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<Powers> binder;

    private Powers powers;

    private final PowersService powersService;

    public PowersView(PowersService powersService) {
        this.powersService = powersService;
        addClassNames("powers-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("powerName").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("strenghtLvl").setAutoWidth(true);
        grid.addColumn("isRare").setAutoWidth(true);
        grid.setItems(query -> powersService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(POWERS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PowersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Powers.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        addNew.addClickListener(e -> {
            this.powers = new Powers();
            binder.readBean(this.powers);
        });

        delete.addClickListener(e -> {
            if (this.powers != null && this.powers.getId() != null) {
                powersService.delete(this.powers.getId());
                clearForm();
                refreshGrid();
                Notification.show("Power deleted");
            }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.powers == null) {
                    this.powers = new Powers();
                }
                binder.writeBean(this.powers);
                powersService.save(this.powers);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PowersView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> powersId = event.getRouteParameters().get(POWERS_ID).map(Long::parseLong);
        if (powersId.isPresent()) {
            Optional<Powers> powersFromBackend = powersService.get(powersId.get());
            if (powersFromBackend.isPresent()) {
                populateForm(powersFromBackend.get());
            } else {
                Notification.show(String.format("The requested powers was not found, ID = %s", powersId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PowersView.class);
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
        powerName = new TextField("Power Name");
        description = new TextField("Description");
        type = new TextField("Type");
        strenghtLvl = new TextField("Strenght Lvl");
        isRare = new TextField("Is Rare");
        formLayout.add(powerName, description, type, strenghtLvl, isRare);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addNew.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
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

    private void populateForm(Powers value) {
        this.powers = value;
        binder.readBean(this.powers);

    }
}
