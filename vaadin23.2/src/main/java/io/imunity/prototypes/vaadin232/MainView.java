package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@Route
@PermitAll
public class MainView extends Composite<VerticalLayout> {

    public MainView() {
        getContent().add(
                new Button("Does it work?", e -> Notification.show("Yes, it works 232!"))
        );
    }

}
