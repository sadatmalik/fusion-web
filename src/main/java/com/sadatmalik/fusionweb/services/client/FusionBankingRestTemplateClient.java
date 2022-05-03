package com.sadatmalik.fusionweb.services.client;

import com.sadatmalik.fusionweb.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

/**
 * Example of invoking services with a Load Balancer–aware Spring REST template.
 *
 * This is one of the more common mechanisms for interacting with the Load Balancer via Spring.
 * To use a Load Balancer–aware RestTemplate class, we need to define a RestTemplate bean with
 * a Spring Cloud @LoadBalanced annotation.
 *
 * @see com.sadatmalik.fusionweb.FusionWebPrototypeApplication
 *
 * Using the backed RestTemplate class pretty much behaves like a standard Spring RestTemplate
 * class, except for one small difference in how the URL for the target service is defined.
 *
 * Rather than using the physical location of the service in the RestTemplate call, we can
 * build the target URL using the Eureka service ID of the service you want to call.
 *
 * @author sadatmalik
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FusionBankingRestTemplateClient {

    private final RestTemplate restTemplate;

    /**
     * When using a Load Balancer–backed RestTemplate, we build the target URL with the
     * Eureka service ID.
     *
     * The Load Balancer–enabled RestTemplate class parses the URL passed into it and uses
     * whatever is passed in as the server name as the key to query the Load Balancer for
     * an instance of a service.
     *
     * The actual service location and port are abstracted from the developer. The Spring
     * Cloud Load Balancer round-robin load balances all requests among all the service
     * instances.
     *
     * @return
     */
    public String getAuthorizationUrl() {
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://fusion-banking/get-auth-url",
                        HttpMethod.GET,
                        null, String.class);
        return restExchange.getBody();
    }

    public String getAccessToken(@PathVariable String authCode) {
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://fusion-banking/get-user-access-token/" + authCode,
                        HttpMethod.GET,
                        null, String.class);
        return restExchange.getBody();
    }

    public Account[] getUserAccounts() {
        ResponseEntity<Account[]> restExchange =
                restTemplate.exchange(
                        "http://fusion-banking/get-user-accounts",
                        HttpMethod.GET,
                        null, Account[].class);

        return restExchange.getBody();
    }

}