package com.example.application.views.heroprofile;

import com.example.application.data.HeroProfile;
import com.example.application.services.HeroProfileService;
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

@PageTitle("Heroprofile")
@Route("heroprofile/:heroProfileID?/:action?(edit)")
@Menu(order = 2, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
public class HeroprofileView extends Div implements BeforeEnterObserver {

    private final String HEROPROFILE_ID = "heroProfileID";
    private final String HEROPROFILE_EDIT_ROUTE_TEMPLATE = "heroprofile/%s/edit";

    private final Grid<HeroProfile> grid = new Grid<>(HeroProfile.class, false);

    private TextField weakness;
    private TextField threatLvl;
    private TextField mentor;
    private TextField secretBase;
    private TextField firstMission;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<HeroProfile> binder;

    private HeroProfile heroProfile;

    private final HeroProfileService heroProfileService;

    public HeroprofileView(HeroProfileService heroProfileService) {
        this.heroProfileService = heroProfileService;
        addClassNames("heroprofile-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("weakness").setAutoWidth(true);
        grid.addColumn("threatLvl").setAutoWidth(true);
        grid.addColumn("mentor").setAutoWidth(true);
        grid.addColumn("secretBase").setAutoWidth(true);
        grid.addColumn("firstMission").setAutoWidth(true);
        grid.setItems(query -> heroProfileService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(HEROPROFILE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(HeroprofileView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(HeroProfile.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.heroProfile == null) {
                    this.heroProfile = new HeroProfile();
                }
                binder.writeBean(this.heroProfile);
                heroProfileService.save(this.heroProfile);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(HeroprofileView.class);
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
        Optional<Long> heroProfileId = event.getRouteParameters().get(HEROPROFILE_ID).map(Long::parseLong);
        if (heroProfileId.isPresent()) {
            Optional<HeroProfile> heroProfileFromBackend = heroProfileService.get(heroProfileId.get());
            if (heroProfileFromBackend.isPresent()) {
                populateForm(heroProfileFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested heroProfile was not found, ID = %s", heroProfileId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(HeroprofileView.class);
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
        weakness = new TextField("Weakness");
        threatLvl = new TextField("Threat Lvl");
        mentor = new TextField("Mentor");
        secretBase = new TextField("Secret Base");
        firstMission = new TextField("First Mission");
        formLayout.add(weakness, threatLvl, mentor, secretBase, firstMission);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
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

    private void populateForm(HeroProfile value) {
        this.heroProfile = value;
        binder.readBean(this.heroProfile);

    }
}
