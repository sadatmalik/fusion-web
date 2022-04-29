package com.sadatmalik.fusionweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Main Spring bootstrap application. Launches Fusion Web.
 *
 * Note the @EnableDiscoveryClient annotation triggers Spring Cloud to enable the application
 * to use the Discovery Client and the Spring Cloud Load Balancer libraries. These are both
 * used to discover the Fusion Banking service and connect to and call its Rest API via a load
 * balanced rest template.
 *
 * @author sadatmalik
 */
@SpringBootApplication
@EnableDiscoveryClient
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
