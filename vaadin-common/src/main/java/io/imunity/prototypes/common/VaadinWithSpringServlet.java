/*
 * Copyright (c) 2018 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.prototypes.common;

import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;

public class VaadinWithSpringServlet extends VaadinServlet {

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		SpringVaadin2XServletService service = new SpringVaadin2XServletService(this, deploymentConfiguration, SpringContextProvider.getContext());
		service.init();
		return service;
	}
}
