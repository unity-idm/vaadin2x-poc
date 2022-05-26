package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.di.ResourceProvider;
import io.imunity.prototypes.common.CustomResourceProvider;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResourceProvider232 extends CustomResourceProvider {

	public ResourceProvider232() throws URISyntaxException {
		super("vaadin-common");
	}

}
