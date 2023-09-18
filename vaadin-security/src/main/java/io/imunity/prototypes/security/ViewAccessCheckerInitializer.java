package io.imunity.prototypes.security;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.ViewAccessChecker;
import org.jsoup.nodes.Document;

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
		serviceInitEvent.addIndexHtmlRequestListener(response -> {
			Document document = response.getDocument();
			document.body().append("<script>window.sessionStorage.setItem(\"redirect-url\", window.location.href);</script>");
		});
	}
}
