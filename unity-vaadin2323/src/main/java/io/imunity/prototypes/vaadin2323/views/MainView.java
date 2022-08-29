package io.imunity.prototypes.vaadin2323.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.PermitAll;

@PermitAll
@Route
@CssImport("./styles/view/main-view.css")
public class MainView extends Composite<VerticalLayout> implements View {

    public MainView() {
        VaadinSession current = VaadinSession.getCurrent();
        getContent().add(
            new Button("Logout",e -> current.getSession().invalidate()),
            new Button("Go to Ala View",e -> UI.getCurrent().navigate(AlaView.class)),
            new Button("Does it work?", e -> Notification.show("Yes, it works 231!"))
        );
    }

}
