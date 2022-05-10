package io.imunity.prototypes.vaadin232;

import com.vaadin.flow.di.ResourceProvider;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResourceProvider232 implements ResourceProvider {
	private final Map<String, CachedStreamData> cache = new ConcurrentHashMap<>();
	private final String currentPath;

	public ResourceProvider232() throws URISyntaxException {
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

	@Override
	public URL getApplicationResource(String path) {
		return getApplicationResources(path).stream().findAny().orElse(null);
	}

	@Override
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

	@Override
	public URL getClientResource(String path) {
		return this.getApplicationResource(path);
	}

	@Override
	public InputStream getClientResourceAsStream(String path) throws IOException {
		CachedStreamData cached = this.cache.computeIfAbsent(path, (key) -> {
			URL url = this.getClientResource(key);
			try {
				InputStream stream = url.openStream();
				CachedStreamData cachedStreamData;
				try {
					ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
					IOUtils.copy(stream, tempBuffer);
					cachedStreamData = new CachedStreamData(tempBuffer.toByteArray(), null);
				} catch (Throwable throwable) {
					if (stream != null) {
						try {
							stream.close();
						} catch (Throwable var6) {
							throwable.addSuppressed(var6);
						}
					}

					throw throwable;
				}

				stream.close();

				return cachedStreamData;
			} catch (IOException ioException) {
				return new CachedStreamData(null, ioException);
			}
		});

		IOException exception = cached.exception;
		if (exception == null) {
			return new ByteArrayInputStream(cached.data);
		} else {
			throw exception;
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
