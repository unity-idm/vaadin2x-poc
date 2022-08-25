/*
 * Copyright (c) 2013 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE.txt file for licensing information.
 */
package io.imunity.prototypes.vaadin2323;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.unity.engine.api.endpoint.EndpointFactory;
import pl.edu.icm.unity.engine.api.endpoint.EndpointInstance;
import pl.edu.icm.unity.engine.api.server.AdvertisedAddressProvider;
import pl.edu.icm.unity.engine.api.server.NetworkServer;
import pl.edu.icm.unity.types.endpoint.EndpointTypeDescription;

import java.util.Collections;

@Component
public class Vaadin23EndpointFactory implements EndpointFactory
{
	public static final String NAME = "VAADIN23POC";
	public static final String SERVLET_PATH = "/v23";
	public static final EndpointTypeDescription TYPE = new EndpointTypeDescription(NAME,
			"User-oriented account management web interface", Vaadin23Authentication.NAME,
			Collections.singletonMap(SERVLET_PATH, "User home endpoint"));

	private final NetworkServer server;
	private final AdvertisedAddressProvider advertisedAddrProvider;

	@Autowired
	public Vaadin23EndpointFactory(NetworkServer server,
	                               AdvertisedAddressProvider advertisedAddrProvider)
	{
		this.server = server;
		this.advertisedAddrProvider = advertisedAddrProvider;
	}

	@Override
	public EndpointTypeDescription getDescription()
	{
		return TYPE;
	}

	@Override
	public EndpointInstance newInstance()
	{
		return new Vaadin23Endpoint(server, advertisedAddrProvider, SERVLET_PATH);
	}
}
