package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.router.RouteBaseData;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import io.imunity.prototypes.vaadin2323.views.LoginView;
import org.springframework.stereotype.Component;

import static io.imunity.prototypes.vaadin2323.views.LoginView.LOGIN_VIEW_ROUTE;

@Component
public class ViewAccessCheckerInitializer implements VaadinServiceInitListener {

	private UnityViewAccessChecker viewAccessChecker;

	public ViewAccessCheckerInitializer() {
		viewAccessChecker = new UnityViewAccessChecker();
	}

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
		serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
			uiInitEvent.getUI().addBeforeEnterListener(viewAccessChecker);
		});
		configureLoginViewAliases();
	}

	private void configureLoginViewAliases() {
		RouteConfiguration configuration =
			RouteConfiguration.forApplicationScope();
		configuration.getAvailableRoutes().stream()
			.map(RouteBaseData::getTemplate)
			.filter(template -> !template.startsWith(LOGIN_VIEW_ROUTE) && !template.isBlank())
			.map(template -> LOGIN_VIEW_ROUTE + "/" + template)
			.forEach(template -> configuration.setRoute(template, LoginView.class));
	}
}
