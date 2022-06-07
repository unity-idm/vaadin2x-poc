package io.imunity.prototypes.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExampleMockAuthenticationService {
	private static final Map<String, String> USERS = new HashMap<>(Map.of("admin", "admin", "ala", "ala"));

	public static boolean authenticate(String username, String password) {
		VaadinServletRequest request = VaadinServletRequest.getCurrent();
		if (request == null) {
			return false;
		}
		if(Optional.ofNullable(USERS.get(username)).filter(pass-> pass.equals(password)).isPresent()) {
			WrappedSession session = VaadinSession.getCurrent().getSession();
			session.setAttribute("login", username);
			session.setAttribute("authenticated", true);
			return true;
		}
		return false;
	}

	public static boolean isAuthenticated() {
		Boolean authenticated = (Boolean)VaadinSession.getCurrent().getSession().getAttribute("authenticated");
		return authenticated != null && authenticated;
	}

	public static void changePassword(String username, String password) {
		if (USERS.get(username) != null)
			USERS.put(username, password);
	}

	public static void logout() {
		VaadinSession.getCurrent().getSession().invalidate();
		UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletContext().getContextPath());
	}
}
