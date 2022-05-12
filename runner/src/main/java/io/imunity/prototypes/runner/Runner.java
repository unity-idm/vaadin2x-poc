package io.imunity.prototypes.runner;


import io.imunity.prototypes.vaadin231.Vaadin231WebApp;
import io.imunity.prototypes.vaadin232.Vaadin232WebApp;
import io.imunity.prototypes.vaadin8.Vaadin8WebApp;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

class Runner {
	public static void main(String... args) throws Exception {
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		WebAppContext vaadin8webAppContext = new Vaadin8WebApp().getWebAppContext("/v8");

		WebAppContext vaadin231webAppContext = new Vaadin231WebApp().getWebAppContext("/v231");
		vaadin231webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		WebAppContext vaadin232webAppContext = new Vaadin232WebApp().getWebAppContext("/v232");
		vaadin232webAppContext.addFilter(AuthenticationFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		contexts.setHandlers(new Handler[] {vaadin231webAppContext, vaadin232webAppContext, vaadin8webAppContext});

		Server server = new Server(8080);
		server.setHandler(contexts);

		server.start();
		server.join();
	}
}
