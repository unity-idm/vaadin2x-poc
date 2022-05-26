package io.imunity.prototypes.vaadin231;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("ala")
public class Main2View extends Composite<VerticalLayout> {

    public Main2View() {
        getContent().add(
                new Button("Is Ala alive?",
                        e -> Notification.show("Ala!"))
        );
    }

}
