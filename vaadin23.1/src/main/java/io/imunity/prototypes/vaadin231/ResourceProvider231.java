package io.imunity.prototypes.vaadin231;

import io.imunity.prototypes.common.CustomResourceProvider;

import java.net.URISyntaxException;


public class ResourceProvider231 extends CustomResourceProvider
{

	public ResourceProvider231() throws URISyntaxException {
		super("vaadin-elements", "vaadin-common", "vaadin-security");
	}
}
