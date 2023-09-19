package io.imunity.prototypes.vaadin8;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

class VaadinServletServiceWithoutAtmosphere extends VaadinServletService
{
	VaadinServletServiceWithoutAtmosphere(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		super(servlet, deploymentConfiguration);
	}

	protected boolean isAtmosphereAvailable() {
		return false;
	}
}
