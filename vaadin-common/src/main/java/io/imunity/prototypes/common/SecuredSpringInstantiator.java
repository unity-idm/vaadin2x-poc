package io.imunity.prototypes.common;

import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.SpringInstantiator;
import io.imunity.prototypes.security.ViewAccessCheckerInitializer;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

class SecuredSpringInstantiator extends SpringInstantiator
{
	public SecuredSpringInstantiator(VaadinService service, ApplicationContext context)
	{
		super(service, context);
	}

	@Override
	public Stream<VaadinServiceInitListener> getServiceInitListeners() {
		Stream<VaadinServiceInitListener> serviceInitListeners = super.getServiceInitListeners();
		return Stream.concat(serviceInitListeners, Stream.of(new ViewAccessCheckerInitializer()));
	}
}
