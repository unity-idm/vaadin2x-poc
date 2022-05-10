package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/ola")
public class Main2View extends Composite<VerticalLayout> {

    public Main2View() {
        getContent().add(
                new Button("Does Ola alive?", e -> Notification.show("Ola!"))
        );
    }

}
