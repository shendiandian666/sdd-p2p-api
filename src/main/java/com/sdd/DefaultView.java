package com.sdd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DefaultView extends WebMvcConfigurerAdapter {

	private Logger logger = LogManager.getLogger(getClass());

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		logger.info("sdd setViewName...");
		registry.addViewController("/").setViewName("redirect:/app");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}
	
}
