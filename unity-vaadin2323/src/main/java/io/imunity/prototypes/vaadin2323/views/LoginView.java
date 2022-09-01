package io.imunity.prototypes.vaadin2323.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.imunity.prototypes.vaadin2323.Vaadin23Authentication;
import io.imunity.prototypes.vaadin2323.Vaadin23WebAppContext;
import pl.edu.icm.unity.engine.api.authn.AuthenticationResult;
import pl.edu.icm.unity.engine.api.authn.remote.AuthenticationTriggeringContext;

import java.util.Collection;

import static io.imunity.prototypes.vaadin2323.Vaadin23Authentication.Context.LOGIN;

@Route(LoginView.LOGIN_VIEW_ROUTE)
@PageTitle("Sign In")
public class LoginView extends Composite<VerticalLayout> implements View {
	public static final String LOGIN_VIEW_ROUTE = "signin";
	public LoginView() {
		getContent().addClassName("login-view");
		getContent().setSizeFull();
		getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		getContent().setAlignItems(FlexComponent.Alignment.CENTER);

		AuthenticationCallback23 authenticationCallback23 = new AuthenticationCallback23();

		Vaadin23Authentication retrieval =
			(Vaadin23Authentication) Vaadin23WebAppContext.getCurrentWebAppAuthentications().iterator().next().getFirstFactorAuthenticators().iterator().next()
			.getRetrieval();
		Collection<Vaadin23Authentication.Vaadin23AuthenticationUI> uiInstance = retrieval.createUIInstance(LOGIN);
		uiInstance.forEach(x -> x.setAuthenticationCallback(authenticationCallback23));

		getContent().add(uiInstance.stream().map(Vaadin23Authentication.Vaadin23AuthenticationUI::getComponent).toArray(Component[]::new));
	}

	class AuthenticationCallback23 implements Vaadin23Authentication.AuthenticationCallback {
		@Override
		public void onStartedAuthentication() {

		}

		@Override
		public void onCompletedAuthentication(AuthenticationResult result) {
			if (result.getStatus().equals(AuthenticationResult.Status.success)) {
				UI.getCurrent().getPage().reload();
			}
			if (result.getStatus().equals(AuthenticationResult.Status.deny)) {
				Dialog dialog = new Dialog(new Label(result.getErrorResult().toString()));
				dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
				dialog.open();
				getContent().add(dialog);
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
