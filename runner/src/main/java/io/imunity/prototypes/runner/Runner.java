package io.imunity.prototypes.runner;


import io.imunity.prototypes.common.VaadinWithSpringServlet;
import io.imunity.prototypes.vaadin231.ResourceProvider231;
import io.imunity.prototypes.vaadin232.ResourceProvider232;
import io.imunity.prototypes.vaadin8.MyUI;
import io.imunity.prototypes.vaadin8.ResourceProvider8;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import java.util.EnumSet;

import static io.imunity.prototypes.runner.WebAppContextFactory.getWebAppContext;

class Runner {

	public static void main(String... args) throws Exception {
		ContextHandlerCollection contexts = new ContextHandlerCollection();

		ResourceProvider8 resourceProvider8 = new ResourceProvider8();
		org.eclipse.jetty.ee8.webapp.WebAppContext vaadin8webAppContext = WebAppVaadin8ContextFactory.getWebAppContext(
			"/v8",
			resourceProvider8.getChosenClassPathElement(),
			resourceProvider8.getClientResource("VAADIN/themes/").toURI()
		);
		vaadin8webAppContext.addServlet(MyUI.MyUIServlet.class, "/*");

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

		contexts.setHandlers(vaadin8webAppContext.get(), vaadin231webAppContext, vaadin232webAppContext);

		Server server = new Server(8080);
		server.setHandler(contexts);

		server.start();
		server.join();
	}
}
