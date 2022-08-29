package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import io.imunity.prototypes.vaadin2323.views.LoginView;
import io.imunity.prototypes.vaadin2323.views.View;
import org.apache.logging.log4j.Logger;
import pl.edu.icm.unity.base.utils.Log;

import javax.servlet.http.HttpServletRequest;

import static com.vaadin.flow.server.auth.ViewAccessChecker.SESSION_STORED_REDIRECT;
import static io.imunity.prototypes.vaadin2323.views.LoginView.LOGIN_VIEW_ROUTE;
import static java.util.Optional.ofNullable;

/**
 * Inspired by com.vaadin.flow.server.auth.ViewAccessChecker
 */
class UnityViewAccessChecker implements BeforeEnterListener {
	private final Logger log = Log.getLogger(Log.U_SERVER_WEB, UnityViewAccessChecker.class);

	private final AccessAnnotationChecker accessAnnotationChecker;

	public UnityViewAccessChecker() {
		this.accessAnnotationChecker = new AccessAnnotationChecker();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		Class<?> targetView = beforeEnterEvent.getNavigationTarget();

		VaadinServletRequest vaadinServletRequest = VaadinServletRequest
			.getCurrent();
		if (vaadinServletRequest == null) {
			log.warn("Preventing navigation to " + targetView.getName()
				+ " because no HTTP request is available for checking access.");
			beforeEnterEvent.rerouteToError(NotFoundException.class);
			return;
		}
		if (!View.class.isAssignableFrom(targetView)) {
			beforeEnterEvent.forwardTo(LOGIN_VIEW_ROUTE);
			log.debug("Unknown url, forward to  {}", LOGIN_VIEW_ROUTE);
			return;
		}

		HttpServletRequest httpServletRequest = vaadinServletRequest
			.getHttpServletRequest();
		log.debug("Checking access for view {}", targetView.getName());
		boolean authenticated = (boolean) ofNullable(httpServletRequest.getSession().getAttribute("authenticated"))
			.orElse(false);
		if (targetView == LoginView.class && !authenticated) {
			log.debug("Allowing access for login view {}", targetView.getName());
			return;
		}
		if (targetView == LoginView.class && authenticated) {
			String path = beforeEnterEvent.getLocation().getPath()
				.replace("/" + LOGIN_VIEW_ROUTE, "")
				.replace(LOGIN_VIEW_ROUTE, "");
			beforeEnterEvent.forwardTo(path);
			log.debug("User authenticated. Redirect to {}", path);
			return;
		}

		boolean hasAccess = accessAnnotationChecker.hasAccess(targetView, httpServletRequest);

		if (hasAccess) {
			log.debug("Allowed access to view {}", targetView.getName());
			return;
		}

		log.debug("Denied access to view {}", targetView.getName());
		if (httpServletRequest.getUserPrincipal() == null) {
			httpServletRequest.getSession().setAttribute(SESSION_STORED_REDIRECT, beforeEnterEvent.getLocation().getPathWithQueryParameters());
			String location = LOGIN_VIEW_ROUTE + "/" + beforeEnterEvent.getLocation().getPath();
			beforeEnterEvent.forwardTo(location);
		} else {
			beforeEnterEvent.rerouteToError(NotFoundException.class, "Access denied");
		}
	}
}
