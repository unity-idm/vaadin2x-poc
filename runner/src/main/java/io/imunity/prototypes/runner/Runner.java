package io.imunity.prototypes.runner;


import io.imunity.prototypes.common.VaadinWithSpringServlet;
import io.imunity.prototypes.vaadin231.ResourceProvider231;
import io.imunity.prototypes.vaadin232.ResourceProvider232;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.EnumSet;

import static io.imunity.prototypes.runner.WebAppContextFactory.getWebAppContext;

class Runner {
	public static void main(String... args) throws Exception {
		ContextHandlerCollection contexts = new ContextHandlerCollection();

		ResourceProvider231 resourceProvider231 = new ResourceProvider231();
		WebAppContext vaadin231webAppContext = getWebAppContext(
			"/v231",
			resourceProvider231.getChosenClassPathElement(),
			resourceProvider231.getClientResource("META-INF/resources/").toURI()
		);
		vaadin231webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));
		vaadin231webAppContext.addServlet(VaadinWithSpringServlet.class, "/*");

		ResourceProvider232 resourceProvider232 = new ResourceProvider232();
		WebAppContext vaadin232webAppContext = getWebAppContext(
			"/v232",
			resourceProvider232.getChosenClassPathElement(),
			resourceProvider232.getClientResource("META-INF/resources/").toURI()
		);
		vaadin232webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));
		vaadin232webAppContext.addServlet(VaadinWithSpringServlet.class, "/*");

		contexts.setHandlers(new Handler[] {vaadin231webAppContext, vaadin232webAppContext});

		Server server = new Server(8080);
		server.setHandler(contexts);

		server.start();
		server.join();
	}
}
