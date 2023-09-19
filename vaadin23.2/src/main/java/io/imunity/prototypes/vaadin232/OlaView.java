package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PermitAll
@Route("/ola")
public class OlaView extends Composite<VerticalLayout> {

    public OlaView() {
        getContent().add(
                new Button("Is Ola alive?", e -> Notification.show("Ola!"))
        );
    }

}
