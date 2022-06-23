/*
 * Copyright (c) 2013 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE.txt file for licensing information.
 */
package io.imunity.prototypes.vaadin823;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.edu.icm.unity.MessageSource;
import pl.edu.icm.unity.engine.api.endpoint.EndpointFactory;
import pl.edu.icm.unity.engine.api.endpoint.EndpointInstance;
import pl.edu.icm.unity.engine.api.server.AdvertisedAddressProvider;
import pl.edu.icm.unity.engine.api.server.NetworkServer;
import pl.edu.icm.unity.types.endpoint.EndpointTypeDescription;
import pl.edu.icm.unity.webui.authn.VaadinAuthentication;
import pl.edu.icm.unity.webui.authn.remote.RemoteRedirectedAuthnResponseProcessingFilter;

import java.util.Collections;

@Component
public class Vaadin823EndpointFactory implements EndpointFactory
{
	public static final String NAME = "VAADIN823POC";
	public static final String SERVLET_PATH = "/v823";
	public static final EndpointTypeDescription TYPE = new EndpointTypeDescription(NAME,
			"User-oriented account management web interface", VaadinAuthentication.NAME,
			Collections.singletonMap(SERVLET_PATH, "User home endpoint"));

	private ApplicationContext applicationContext;
	private NetworkServer server;
	private MessageSource msg;
	private AdvertisedAddressProvider advertisedAddrProvider;
	private RemoteRedirectedAuthnResponseProcessingFilter remoteAuthnResponseProcessingFilter;

	@Autowired
	public Vaadin823EndpointFactory(ApplicationContext applicationContext,
	                                NetworkServer server,
	                                AdvertisedAddressProvider advertisedAddrProvider,
	                                MessageSource msg,
	                                RemoteRedirectedAuthnResponseProcessingFilter remoteAuthnResponseProcessingFilter)
	{
		this.applicationContext = applicationContext;
		this.server = server;
		this.msg = msg;
		this.advertisedAddrProvider = advertisedAddrProvider;
		this.remoteAuthnResponseProcessingFilter = remoteAuthnResponseProcessingFilter;
	}

	@Override
	public EndpointTypeDescription getDescription()
	{
		return TYPE;
	}

	@Override
	public EndpointInstance newInstance()
	{
		return new Vaadin823Endpoint(server, advertisedAddrProvider, msg, applicationContext,
			"vaadin823",
				SERVLET_PATH, remoteAuthnResponseProcessingFilter);
	}
}
