package io.imunity.prototypes.vaadin231;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.imunity.prototypes.adds.ExtraComponent;
import io.imunity.prototypes.common.CommonService;
import jakarta.annotation.security.PermitAll;


@Route
@PermitAll
public class MainView extends Composite<VerticalLayout> {

    public MainView(CommonService commonService) {
        getContent().add(
                new Button("Does it work?",
                        e -> Notification.show("Yes, it works 231! - " + commonService.getData())),
            new CheckboxWithError("Alala"),
            new ExtraComponent()
        );
    }

}
