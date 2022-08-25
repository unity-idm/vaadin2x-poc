package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import pl.edu.icm.unity.engine.api.authn.AuthenticationResult;
import pl.edu.icm.unity.engine.api.authn.remote.AuthenticationTriggeringContext;

import java.util.Collection;

import static io.imunity.prototypes.vaadin2323.Vaadin23Authentication.Context.LOGIN;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

	public LoginView() {
		addClassName("login-view");
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.CENTER);
		setAlignItems(Alignment.CENTER);

		if(isAuthenticated()) {
			UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletContext().getContextPath());
			return;
		}

		AuthenticationCallback23 authenticationCallback23 = new AuthenticationCallback23();

		Vaadin23Authentication retrieval =
			(Vaadin23Authentication) Vaadin23WebAppContext.getCurrentWebAppAuthentications().iterator().next().getFirstFactorAuthenticators().iterator().next()
			.getRetrieval();
		Collection<Vaadin23Authentication.VaadinAuthenticationUI> uiInstance = retrieval.createUIInstance(LOGIN);
		uiInstance.forEach(x -> x.setAuthenticationCallback(authenticationCallback23));

		add(uiInstance.stream().map(Vaadin23Authentication.VaadinAuthenticationUI::getComponent).toArray(Component[]::new));
	}

	private void redirectToMainView() {
		UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletContext().getContextPath());
	}

	public boolean isAuthenticated() {
		Boolean authenticated = (Boolean) VaadinSession.getCurrent().getSession().getAttribute("authenticated");
		return authenticated != null && authenticated;
	}

	class AuthenticationCallback23 implements Vaadin23Authentication.AuthenticationCallback {
		@Override
		public void onStartedAuthentication() {

		}

		@Override
		public void onCompletedAuthentication(AuthenticationResult result) {
			if (result.getStatus().equals(AuthenticationResult.Status.success)) {
				redirectToMainView();
			}
			if (result.getStatus().equals(AuthenticationResult.Status.deny)) {
				Dialog dialog = new Dialog(new Label(result.getErrorResult().toString()));
				dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
				dialog.open();
				add(dialog);
			}
		}

		@Override
		public void onCancelledAuthentication() {

		}

		@Override
		public AuthenticationTriggeringContext getTriggeringContext() {
			return null;
		}
	}
}
