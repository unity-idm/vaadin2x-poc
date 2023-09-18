package io.imunity.prototypes.runner;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.util.resource.URLResourceFactory;

import java.net.URI;
import java.util.Set;

import static org.eclipse.jetty.ee10.webapp.MetaInfConfiguration.CONTAINER_JAR_PATTERN;

class WebAppContextFactory {

	private static final String DEFAULT_CONTAINER_INCLUDE_JAR_PATTERN = ".*/jakarta.servlet-[^/]*\\.jar$|.*/jetty-jakarta-servlet-api-[^/]*\\.jar$|.*jakarta.servlet.jsp.jstl-[^/]*\\.jar|.*taglibs-standard-[^/]*\\.jar$";

	static WebAppContext getWebAppContext(String contextPath, Set<String> classPathElements, URI webResourceRootUri) throws Exception {
		WebAppContext context = new WebAppContext();
		context.setBaseResource(new URLResourceFactory().newResource(webResourceRootUri));
		context.setContextPath(contextPath);
		String pattern = addPattern(JarGetter.getJarsRegex(classPathElements), DEFAULT_CONTAINER_INCLUDE_JAR_PATTERN);

		String alll = ".*\\.jar|.*/classes/.*";
		context.setAttribute(CONTAINER_JAR_PATTERN, alll);
		context.setConfigurationDiscovered(true);

		return context;
	}

	private static String addPattern(String s, String pattern)
	{
		if (s == null)
			s = "";
		else
			s = s.trim();

		if (!s.contains(pattern))
		{
			if (s.length() != 0)
				s = s + "|";
			s = s + pattern;
		}

		return s;
	}

}
