package io.imunity.prototypes.security;

import com.vaadin.flow.di.DefaultInstantiator;
import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.di.InstantiatorFactory;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;

import java.util.stream.Stream;

public final class CustomInstantiatorFactory implements InstantiatorFactory {
	@Override
	public Instantiator createInstantitor(VaadinService vaadinService) {
		return new CustomDefaultInstantiator(vaadinService);
	}

	static class CustomDefaultInstantiator extends DefaultInstantiator {
		public CustomDefaultInstantiator(VaadinService service) {
			super(service);
		}

		public Stream<VaadinServiceInitListener> getServiceInitListeners() {
			return Stream.of(new ViewAccessCheckerInitializer());
		}

	}
}
