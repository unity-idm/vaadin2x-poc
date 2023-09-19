package io.imunity.prototypes.runner;

import org.eclipse.jetty.ee8.webapp.WebAppContext;
import org.eclipse.jetty.util.resource.URLResourceFactory;

import java.net.URI;
import java.util.Set;

import static org.eclipse.jetty.ee10.webapp.MetaInfConfiguration.CONTAINER_JAR_PATTERN;

class WebAppVaadin8ContextFactory
{

	static WebAppContext getWebAppContext(String contextPath, Set<String> classPathElements, URI webResourceRootUri) throws Exception {
		WebAppContext context = new WebAppContext();
		context.setBaseResource(new URLResourceFactory().newResource(webResourceRootUri));
		context.setContextPath(contextPath);

		context.setAttribute(CONTAINER_JAR_PATTERN, JarGetter.getJarsRegex(classPathElements));
		context.setConfigurationDiscovered(true);

		return context;
	}
}
