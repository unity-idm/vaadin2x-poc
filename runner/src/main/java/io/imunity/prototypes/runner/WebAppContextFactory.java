package io.imunity.prototypes.runner;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URI;
import java.util.Set;

class WebAppContextFactory {
	static WebAppContext getWebAppContext(String contextPath, Set<String> classPathElements, URI webResourceRootUri) throws Exception {
		WebAppContext context = new WebAppContext();
		context.setBaseResource(Resource.newResource(webResourceRootUri));
		context.setContextPath(contextPath);
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", JarGetter.getJarsRegex(classPathElements));
		context.setConfigurationDiscovered(true);

		return context;
	}
}
