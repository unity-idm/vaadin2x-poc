package io.imunity.prototypes.vaadin2323.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.PermitAll;

@PermitAll
@Route("ala")
public class AlaView extends Composite<VerticalLayout> {

    public AlaView() {
        VaadinSession current = VaadinSession.getCurrent();
        getContent().add(
                new Button("Logout",e -> current.getSession().invalidate()),
                new Button("Back to Main View",e -> UI.getCurrent().navigate(MainView.class)),
                new Button("Is Ala alive?",e -> Notification.show("Ala!"))
        );
    }

}
