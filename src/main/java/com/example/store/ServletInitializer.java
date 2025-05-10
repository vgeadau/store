package com.example.store;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * ServletInitializer class.
 */
@SuppressWarnings("unused")
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * configures the resources.
	 * @param application SpringApplicationBuilder
	 * @return SpringApplicationBuilder object
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StoreApplication.class);
	}

}
