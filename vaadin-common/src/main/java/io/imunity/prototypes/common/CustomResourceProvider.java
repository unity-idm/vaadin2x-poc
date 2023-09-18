package io.imunity.prototypes.common;

import com.vaadin.flow.di.ResourceProvider;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import static java.util.Arrays.stream;

public abstract class CustomResourceProvider implements ResourceProvider {
	private final Map<String, CachedStreamData> cache = new ConcurrentHashMap<>();
	private final Set<String> chosenClassPathElement;
	private final Set<String> chosenClassPathElementForJetty;

	public CustomResourceProvider(String... chosenModules) throws URISyntaxException {
		String currentClassPathElement = getClass()
			.getProtectionDomain()
			.getCodeSource()
			.getLocation()
			.toURI()
			.toString();

		Set<String> classPathElements = stream(System.getProperty("java.class.path").split(File.pathSeparator))
			.filter(classPathElement -> stream(chosenModules).anyMatch(classPathElement::contains))
			.map(path -> new File(path).toURI().toString())
			.collect(Collectors.toSet());

		classPathElements.add(currentClassPathElement);
		this.chosenClassPathElement = classPathElements;
		this.chosenClassPathElementForJetty = classPathElements.stream()
			.map(CustomResourceProvider::getUrlCompatibleWithJetty)
			.collect(Collectors.toSet());
	}

	public Set<String> getChosenClassPathElement() {
		return chosenClassPathElementForJetty;
	}

	public static String getUrlCompatibleWithJetty(String vale) {
		URL url;
		try
		{
			url = new URL(vale);
		} catch (MalformedURLException e)
		{
			throw new RuntimeException(e);
		}
		StringBuilder strForm = new StringBuilder();
		String protocol = url.getProtocol();
		strForm.append(protocol);
		strForm.append("://");
		strForm.append(url.getFile());
		return strForm.toString();
	}

	@Override
	public URL getApplicationResource(String path) {
		return getApplicationResources(path).stream().findAny().orElse(null);
	}

	@Override
	public List<URL> getApplicationResources(String path) {
		Iterable<URL> iterable = getUrls(path);
		return StreamSupport.stream(iterable.spliterator(), false)
			.filter(url -> chosenClassPathElement.stream().anyMatch(classPathElement -> url.toString().startsWith(classPathElement)))
			.collect(Collectors.toList());
	}

	private Iterable<URL> getUrls(String path) {
		Iterator<URL> urlIterator = getUrlIterator(path);
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

	@Override
	public URL getClientResource(String path) {
		return this.getApplicationResource(path);
	}

	@Override
	public InputStream getClientResourceAsStream(String path) throws IOException {
		CachedStreamData cached = this.cache.computeIfAbsent(path, this::loadResourceStreamAsCachedData);

		IOException exception = cached.exception;
		if (exception == null) {
			return new ByteArrayInputStream(cached.data);
		} else {
			throw exception;
		}
	}

	private CachedStreamData loadResourceStreamAsCachedData(String key) {
		URL url = this.getClientResource(key);
		try(InputStream stream = url.openStream()) {
			CachedStreamData cachedStreamData;
			try {
				ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
				IOUtils.copy(stream, tempBuffer);
				cachedStreamData = new CachedStreamData(tempBuffer.toByteArray(), null);
			} catch (Throwable throwable) {
				if (stream != null) {
					try {
						stream.close();
					} catch (Throwable suppressedThrowable) {
						throwable.addSuppressed(suppressedThrowable);
					}
				}

				throw throwable;
			}

			stream.close();

			return cachedStreamData;
		} catch (IOException ioException) {
			return new CachedStreamData(null, ioException);
		}
	}

	private static class CachedStreamData {
		private final byte[] data;
		private final IOException exception;

		private CachedStreamData(byte[] data, IOException exception) {
			this.data = data;
			this.exception = exception;
		}
	}
}
