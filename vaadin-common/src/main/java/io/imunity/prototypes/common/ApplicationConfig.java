package io.imunity.prototypes.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationConfig
{
	@Bean
	public CommonService departmentService() {
		return new CommonService();
	}
}
