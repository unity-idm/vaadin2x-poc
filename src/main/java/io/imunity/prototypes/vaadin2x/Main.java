package io.imunity.prototypes.vaadin2x;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.startup.ServletContextListeners;

/**
 * Heavily based on work here: https://github.com/mvysny/vaadin14-embedded-jetty-gradle
 * To be heavily refactored...
 */
public final class Main
{

	private static Server server;

	public static void main(String[] args) throws Exception
	{
		start();
		server.join();
	}

	public static void start() throws Exception
	{
		final String contextRoot = "/app1";

		System.setProperty("vaadin.productionMode", "true");

		final WebAppContext context = new WebAppContext();
		context.setBaseResource(findWebRoot());
		context.setContextPath(contextRoot);
		context.addServlet(VaadinServlet.class, "/*");
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*\\.jar|.*/classes/.*");
		context.setConfigurationDiscovered(true);
		context.getServletContext().setExtendedListenerTypes(true);
		context.addEventListener(new ServletContextListeners());

		int port = 8080;

		server = new Server(port);
		server.setHandler(context);
		final Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addBefore(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());
		server.start();
	}

	public static void stop() throws Exception
	{
		server.stop();
		server = null;
	}

	private static Resource findWebRoot() throws MalformedURLException
	{
		// don't look up directory as a resource, it's unreliable:
		// https://github.com/eclipse/jetty.project/issues/4173#issuecomment-539769734
		// instead we'll look up the /webapp/ROOT and retrieve the
		// parent folder from that.
		final URL f = Main.class.getResource("/webapp/ROOT");
		if (f == null)
			throw new IllegalStateException(
					"Invalid state: the resource /webapp/ROOT doesn't exist, has webapp been packaged in as a resource?");
		final String url = f.toString();
		if (!url.endsWith("/ROOT"))
			throw new RuntimeException("Parameter url: invalid value " + url + ": doesn't end with /ROOT");
		System.err.println("/webapp/ROOT is " + f);

		// Resolve file to directory
		URL webRoot = new URL(url.substring(0, url.length() - 5));
		System.err.println("WebRoot is " + webRoot);
		return Resource.newResource(webRoot);
	}
}
