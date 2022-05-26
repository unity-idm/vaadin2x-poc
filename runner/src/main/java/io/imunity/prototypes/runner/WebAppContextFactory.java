package io.imunity.prototypes.runner;

import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.net.URI;
import java.util.EventListener;
import java.util.Set;

class WebAppContextFactory {
	static WebAppContext getWebAppContext(String contextPath, Set<String> classPathElements, URI webResourceRootUri,
	                                      EventListener eventListener) throws Exception {
		WebAppContext context = new WebAppContext();
		context.setBaseResource(Resource.newResource(webResourceRootUri));
		context.setContextPath(contextPath);
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", JarGetter.getJarsRegex(classPathElements));
		context.setConfigurationDiscovered(true);

		context.setConfigurations(new Configuration[]{
			new AnnotationConfiguration(),
			new WebInfConfiguration(),
			new WebXmlConfiguration(),
			new MetaInfConfiguration(),
			new FragmentConfiguration(),
			new EnvConfiguration(),
			new PlusConfiguration(),
			new JettyWebXmlConfiguration()
		});
		context.getServletContext().setExtendedListenerTypes(true);
		if(eventListener != null)
			context.addEventListener(eventListener);

		return context;
	}
}
