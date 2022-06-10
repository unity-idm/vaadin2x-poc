package io.imunity.prototypes.vaadin8;


import io.imunity.prototypes.common.CustomResourceProvider;

import java.net.URISyntaxException;

public class ResourceProvider8 extends CustomResourceProvider {

	public ResourceProvider8() throws URISyntaxException {
		super("vaadin-common", "vaadin-widgets");
	}
}
