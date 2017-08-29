package com.sdd;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;

/**
 * 这个需要使用内置的tomcat才可以
 * @author Administrator
 *
 */
//@Configuration
public class ErrorConfiguration {

	//@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/400");
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401");
				ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
				//ErrorPage errorPage = new ErrorPage(java.lang.Throwable.class, "/500");
				//ErrorPage errorPage1 = new ErrorPage(java.lang.Exception.class, "/500");
				container.addErrorPages(error400Page, error401Page, error403Page, error404Page, error500Page/*, errorPage, errorPage1*/);

			}
		};
	}
	
}
