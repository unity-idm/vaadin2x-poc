/*
 * Copyright (c) 2021 Bixbit - Krzysztof Benedyczak. All rights reserved.
 * See LICENCE.txt file for licensing information.
 */

package io.imunity.prototypes.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextProvider
{
	private static final ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

	public static ApplicationContext getContext()
	{
		return context;
	}
}
