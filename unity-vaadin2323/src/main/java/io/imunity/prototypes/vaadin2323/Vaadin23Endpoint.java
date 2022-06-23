/*
 * Copyright (c) 2013 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE.txt file for licensing information.
 */
package io.imunity.prototypes.vaadin2323;

import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import pl.edu.icm.unity.engine.api.authn.AuthenticationFlow;
import pl.edu.icm.unity.engine.api.endpoint.AbstractWebEndpoint;
import pl.edu.icm.unity.engine.api.endpoint.WebAppEndpointInstance;
import pl.edu.icm.unity.engine.api.server.AdvertisedAddressProvider;
import pl.edu.icm.unity.engine.api.server.NetworkServer;

import javax.servlet.DispatcherType;
import java.net.URI;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

public class Vaadin23Endpoint extends AbstractWebEndpoint implements WebAppEndpointInstance
{
	protected String uiServletPath;
	protected ServletContextHandler context = null;

	public Vaadin23Endpoint(NetworkServer server,
	                        AdvertisedAddressProvider advertisedAddrProvider,
	                        String servletPath)
	{
		super(server, advertisedAddrProvider);
		this.uiServletPath = servletPath;
	}

	@Override
	public void setSerializedConfiguration(String cfg)
	{
	}

	@Override
	public synchronized void updateAuthenticationFlows(List<AuthenticationFlow> authenticators)
	{
	}

	@Override
	public synchronized ServletContextHandler getServletContextHandler()
	{
		context = getServletContextHandlerOverridable();
		return context;
	}

	protected ServletContextHandler getServletContextHandlerOverridable()
	{
		if (context != null)
			return context;

		ServletContextHandler context;
		ResourceProvider23 resourceProvider23;
		try {
			resourceProvider23 = new ResourceProvider23();
			context = getWebAppContext(uiServletPath,
				resourceProvider23.getChosenClassPathElement(),
				resourceProvider23.getClientResource("META-INF/resources/").toURI(),
				new ServletContextListeners()
			);
		} catch (Exception e) {
			return this.context;
		}

		context.setContextPath(description.getEndpoint().getContextAddress());
		context.addFilter(Authentication23Filter.class, "*", EnumSet.of(DispatcherType.REQUEST));

		return context;
	}

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
