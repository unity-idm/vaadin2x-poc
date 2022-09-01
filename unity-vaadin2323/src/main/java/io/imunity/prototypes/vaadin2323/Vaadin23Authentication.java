package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.component.Component;
import pl.edu.icm.unity.engine.api.authn.AuthenticationResult;
import pl.edu.icm.unity.engine.api.authn.remote.AuthenticationTriggeringContext;
import pl.edu.icm.unity.engine.api.endpoint.BindingAuthn;
import pl.edu.icm.unity.types.basic.Entity;
import pl.edu.icm.unity.webui.authn.CredentialResetLauncher;

import java.util.Collection;

public interface Vaadin23Authentication extends BindingAuthn {
	String NAME = "web-vaadin23";


	Collection<Vaadin23AuthenticationUI> createUIInstance(Context context);

	interface AuthenticationCallback {
		void onStartedAuthentication();

		void onCompletedAuthentication(AuthenticationResult var1);

		void onCancelledAuthentication();

		AuthenticationTriggeringContext getTriggeringContext();
	}

	interface Vaadin23AuthenticationUI {
		Component getComponent();

		void setAuthenticationCallback(AuthenticationCallback var1);

		default void setCredentialResetLauncher(CredentialResetLauncher credResetLauncher) {
		}

		void clear();

		String getId();

		void presetEntity(Entity var1);
	}

	enum Context {
		LOGIN,
		REGISTRATION
	}
}
