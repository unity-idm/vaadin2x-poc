package io.imunity.prototypes.vaadin8;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResourceProvider8 {
	private final String currentPath;

	public ResourceProvider8() throws URISyntaxException {
		currentPath = getClass()
			.getProtectionDomain()
			.getCodeSource()
			.getLocation()
			.toURI()
			.toString();
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public URL getApplicationResource(String path) {
		return getApplicationResources(path).stream().findAny().orElse(null);
	}

	public List<URL> getApplicationResources(String path) {
		Iterable<URL> iterable = getUrls(path);
		return StreamSupport.stream(iterable.spliterator(), false)
			.filter(url -> url.toString().startsWith(currentPath))
			.collect(Collectors.toList());
	}

	private Iterable<URL> getUrls(String path) {
		Iterator<URL> urlIterator;
		urlIterator = getUrlIterator(path);
		return () -> urlIterator;
	}

	private Iterator<URL> getUrlIterator(String path) {
		Iterator<URL> urlIterator;
		try {
			urlIterator = getClass().getClassLoader().getResources(path).asIterator();
		} catch (IOException e) {
			urlIterator = Collections.emptyIterator();
		}
		return urlIterator;
	}

	public URL getClientResource(String path) {
		return this.getApplicationResource(path);
	}
}
