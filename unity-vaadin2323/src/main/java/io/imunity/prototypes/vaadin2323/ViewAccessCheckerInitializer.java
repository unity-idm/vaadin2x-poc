package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.ViewAccessChecker;
import org.springframework.stereotype.Component;

@Component
public class ViewAccessCheckerInitializer implements VaadinServiceInitListener {

	private ViewAccessChecker viewAccessChecker;

	public ViewAccessCheckerInitializer() {
		viewAccessChecker = new ViewAccessChecker();
		viewAccessChecker.setLoginView(LoginView.class);
	}

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
		serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
			uiInitEvent.getUI().addBeforeEnterListener(viewAccessChecker);
		});
	}
}
