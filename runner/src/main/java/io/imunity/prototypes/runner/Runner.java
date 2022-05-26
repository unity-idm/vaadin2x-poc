package io.imunity.prototypes.runner;


import com.vaadin.flow.server.startup.ServletContextListeners;
import io.imunity.prototypes.vaadin231.ResourceProvider231;
import io.imunity.prototypes.vaadin232.ResourceProvider232;
import io.imunity.prototypes.vaadin8.ResourceProvider8;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static io.imunity.prototypes.runner.WebAppContextFactory.*;

class Runner {
	public static void main(String... args) throws Exception {
		ContextHandlerCollection contexts = new ContextHandlerCollection();

		ResourceProvider8 resourceProvider8 = new ResourceProvider8();
		WebAppContext vaadin8webAppContext = getWebAppContext(
			"/v8",
			resourceProvider8.getChosenClassPathElement(),
			resourceProvider8.getClientResource("VAADIN/themes/").toURI(),
			null
		);

		ResourceProvider231 resourceProvider231 = new ResourceProvider231();
		WebAppContext vaadin231webAppContext = getWebAppContext(
			"/v231",
			resourceProvider231.getChosenClassPathElement(),
			resourceProvider231.getClientResource("META-INF/resources/").toURI(),
			new ServletContextListeners()
		);
		vaadin231webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		ResourceProvider232 resourceProvider232 = new ResourceProvider232();
		WebAppContext vaadin232webAppContext = getWebAppContext(
			"/v232",
			resourceProvider232.getChosenClassPathElement(),
			resourceProvider232.getClientResource("META-INF/resources/").toURI(),
			new ServletContextListeners()
		);
		vaadin232webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		contexts.setHandlers(new Handler[] {vaadin8webAppContext, vaadin231webAppContext, vaadin232webAppContext});

		Server server = new Server(8080);
		server.setHandler(contexts);

		server.start();
		server.join();
	}
}
