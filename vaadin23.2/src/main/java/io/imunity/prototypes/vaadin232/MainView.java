package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;


@Route
@PermitAll
@CssImport("./styles/view/main-view.css")
public class MainView extends Composite<VerticalLayout> {

    public MainView() {
        VaadinSession current = VaadinSession.getCurrent();
        getContent().add(
                new Button("Does it work?", e -> Notification.show("Yes, it works 232!")),
                new Button("LOGOUT", e -> current.getSession().invalidate())
        );
    }

}
