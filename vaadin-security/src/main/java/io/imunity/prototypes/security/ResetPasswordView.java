package io.imunity.prototypes.security;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("reset")
@AnonymousAllowed
public class ResetPasswordView extends Composite<VerticalLayout> {
	public ResetPasswordView() {
		TextField login = new TextField("username");
		TextField pass1 = new TextField("new password");
		TextField pass2 = new TextField("repeated new password");

		Button button = new Button("Change password", e -> {
			if(pass1.getValue().equals(pass2.getValue())) {
				SecurityUtils.changePassword(login.getValue(), pass1.getValue());
				Notification.show("Password has changed");
			}
		});
		getContent().add(login, pass1, pass2, button);
	}
}
