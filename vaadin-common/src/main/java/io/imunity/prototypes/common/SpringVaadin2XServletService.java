/*
 * Copyright (c) 2021 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.prototypes.common;

import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class SpringVaadin2XServletService extends VaadinServletService
{
	private final ApplicationContext context;

	public SpringVaadin2XServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration, ApplicationContext applicationContext)
	{
		super(servlet, deploymentConfiguration);
		this.context = applicationContext;
	}

	@Override
	protected Optional<Instantiator> loadInstantiators()
	{
		return Optional.of(new SecuredSpringInstantiator(this, context));
	}

	@Override
	public void init() throws ServiceException
	{
		super.init();
		context.getBeansOfType(UIInitListener.class)
				.values()
				.forEach(this::addUIInitListener);
	}
}
