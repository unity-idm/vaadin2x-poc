/*
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.imunity.prototypes.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.di.DefaultInstantiator;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import io.imunity.prototypes.security.ViewAccessCheckerInitializer;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

public class SecuredSpringInstantiator extends DefaultInstantiator {

    private final ApplicationContext context;

    public SecuredSpringInstantiator(VaadinService service, ApplicationContext context) {
        super(service);
        this.context = context;
    }

    @Override
    public Stream<VaadinServiceInitListener> getServiceInitListeners() {
        return Stream.concat(super.getServiceInitListeners(), Stream.of(new ViewAccessCheckerInitializer()));
    }

    @Override
    public <T extends Component> T createComponent(Class<T> componentClass) {
        return context.getAutowireCapableBeanFactory().createBean(componentClass);
    }

    @Override
    public <T> T getOrCreate(Class<T> type) {
        if (context.getBeanNamesForType(type).length == 1)
            return context.getBean(type);
        else if (context.getBeanNamesForType(type).length > 1)
            return createBean(type);
        else
            return context.getAutowireCapableBeanFactory().createBean(type);
    }

    private <T> T createBean(Class<T> type)
    {
        try {
            return context.getAutowireCapableBeanFactory().createBean(type);
        } catch (BeanInstantiationException e) {
            throw new BeanInstantiationException(e.getBeanClass(), "Probably more than one suitable beans for in the context.", e);
        }
    }
}
