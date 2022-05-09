package com.sadatmalik.fusionweb;

import com.sadatmalik.fusionweb.tracking.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Collections;
import java.util.List;

/**
 * Main Spring bootstrap application. Launches Fusion Web.
 *
 * Note the @EnableDiscoveryClient annotation triggers Spring Cloud to enable the application
 * to use the Discovery Client and the Spring Cloud Load Balancer libraries. These are both
 * used to discover the Fusion Banking service and connect to and call its Rest API via a load
 * balanced rest template.
 *
 * @see com.sadatmalik.fusionweb.services.client.FusionBankingDiscoveryClient
 *
 * @EnableFeignClients enables an alternative to the Spring Load Balancer–enabled RestTemplate
 * class by activating Netflix’s Feign client library. The Feign library takes a different approach
 * to calling a REST service. Here we first define a Java interface and then add Spring Cloud
 * annotations to map what Eureka-based service the Spring Cloud Load Balancer will invoke.
 *
 * @see
 *
 * @author sadatmalik
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FusionWebPrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FusionWebPrototypeApplication.class, args);
	}

	/**
	 * Creates a Load Balancer–backed Spring RestTemplate bean.
	 *
	 * Behaves like a standard Spring RestTemplate class, except the URL for the target
	 * service, rather than using the physical location of the service, the target URL
	 * is built using the Discovery (eureka) service ID of the service you want to call.
	 *
	 * We add a UserContextInterceptor to the RestTemplate for the propagation of
	 * correlation ID and other user context headers to downstream services.
	 *
	 * @see com.sadatmalik.fusionweb.services.client.FusionBankingRestTemplateClient
	 * @return load balancer-backed rest template.
	 */
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate template = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
		if (interceptors == null) {
			template.setInterceptors(
					Collections.singletonList(
							new UserContextInterceptor()));
		} else {
			interceptors.add(new UserContextInterceptor());
			template.setInterceptors(interceptors);
		}
		return template;
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
