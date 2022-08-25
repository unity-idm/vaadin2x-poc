/*
 * Copyright (c) 2018 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.prototypes.vaadin2323;

import org.eclipse.jetty.webapp.WebAppContext;
import pl.edu.icm.unity.engine.api.authn.AuthenticationFlow;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Vaadin23WebAppContext extends WebAppContext
{
	private final Supplier<List<AuthenticationFlow>> authentications;

	public Vaadin23WebAppContext(Supplier<List<AuthenticationFlow>> authentications)
	{
		this.authentications = authentications;
	}

	public static List<AuthenticationFlow> getCurrentWebAppAuthentications()
	{
		return Optional.ofNullable(getCurrentWebAppContext())
				.map(context -> (Vaadin23WebAppContext) context)
				.map(context -> context.authentications.get())
				.orElse(List.of());
	}
}
