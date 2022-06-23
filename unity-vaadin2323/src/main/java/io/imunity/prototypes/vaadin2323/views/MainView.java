package io.imunity.prototypes.vaadin2323.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PermitAll
@Route
@CssImport("./styles/view/main-view.css")
public class MainView extends Composite<VerticalLayout> {

    public MainView() {
        getContent().add(
                new Button("Does it work?",
                        e -> Notification.show("Yes, it works 231!"))
        );
    }

}
