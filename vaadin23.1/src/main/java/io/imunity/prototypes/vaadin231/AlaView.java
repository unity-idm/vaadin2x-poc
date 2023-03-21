package io.imunity.prototypes.vaadin231;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PermitAll
@Route("ala")
public class AlaView extends Composite<VerticalLayout> {

    public AlaView() {
        getContent().add(
                new Button("Is Ala alive?",
                        e -> Notification.show("Ala!"))
        );
    }

}
