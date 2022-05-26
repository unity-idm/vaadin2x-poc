package io.imunity.prototypes.vaadin8;


import io.imunity.prototypes.common.CustomResourceProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResourceProvider8 extends CustomResourceProvider {

	public ResourceProvider8() throws URISyntaxException {
		super("vaadin-common");
	}
}
