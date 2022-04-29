package com.sadatmalik.fusionweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Main Spring bootstrap application. Launches Fusion Web.
 *
 * @author sadatmalik
 */
@SpringBootApplication
public class FusionWebPrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FusionWebPrototypeApplication.class, args);
	}

	/**
	 * Simple request logging filter that writes the request URI (and optionally the
	 * query string) to the Commons Log.
	 *
	 * todo move to logging config class.
	 *
	 * @return a configured commons request logging filter.
	 */
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
		loggingFilter.setIncludeClientInfo(true);
		loggingFilter.setIncludeQueryString(true);
		loggingFilter.setIncludePayload(true);
		loggingFilter.setMaxPayloadLength(64000);
		return loggingFilter;
	}

}
