package io.imunity.prototypes.security;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.firitin.util.WebStorage;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver,
	ComponentEventListener<AbstractLogin.LoginEvent> {

	private final LoginForm login = new LoginForm();

	public LoginView() {
		addClassName("login-view");
		setSizeFull();

		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		login.addLoginListener(this);
		Button changePasswordButton = new Button("Change password",
			event -> UI.getCurrent().navigate(ResetPasswordView.class));

		add(new H1("Test Application"), login, changePasswordButton);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if (beforeEnterEvent.getLocation()
			.getQueryParameters()
			.getParameters()
			.containsKey("error")) {
			login.setError(true);
		}
		if(VaadinService.getCurrentRequest().isUserInRole("USER"))
			UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletContext().getContextPath());
	}

	@Override
	public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
		boolean authenticated = ExampleMockAuthenticationService.authenticate(
			loginEvent.getUsername(), loginEvent.getPassword());
		if (authenticated) {
			WebStorage.getItem(
				WebStorage.Storage.sessionStorage,
				"redirect-url",
				value -> UI.getCurrent().getPage().setLocation(value)
			);
		} else {
			login.setError(true);
		}
	}
}
