package com.sadatmalik.fusionweb.services.client;

import com.sadatmalik.fusionweb.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * The Spring Discovery Client offers the lowest level of access to the Load Balancer and the
 * services registered within it. Using the injected Discovery Client, we can query for all the
 * services registered with the Spring Cloud Load Balancer client and their corresponding URLs.
 *
 * To retrieve all instances of the banking services registered with Eureka, we use the
 * getInstances() method, passing in the service key that we’re looking for to retrieve a list
 * of ServiceInstance objects.
 *
 * The ServiceInstance objects holds information about a specific instance of a service, including
 * its hostname, port, and URI.
 *
 * We take the first ServiceInstance class in the list to build a target URL that can then be
 * used to call the service, using a standard Spring RestTemplate to call the banking
 * service and retrieve the required banking data.
 *
 * To use the Discovery Client, we ensure that the FusionWebPrototypeApplication is annotated
 * with @EnableDiscoveryClient.
 *
 * We should only use the Discovery Client when our service needs to query the Load Balancer
 * to understand what services and service instances are registered with it. It becomes our
 * responsibility to choose which returned service instance we’re going to invoke. And it is
 * our responsibility to build the URL that we’ll use to call our service.
 *
 * Note that directly instantiating the RestTemplate class in the code is antithetical to usual
 * Spring REST invocations because we’ll usually have the Spring framework inject the RestTemplate
 * class via the @Autowired annotation.
 *
 * Once we’ve enabled the Spring Discovery Client in the application class via
 * @EnableDiscoveryClient, all REST templates managed by the Spring framework will have a Load
 * Balancer–enabled interceptor injected into those instances. This will change how URLs are
 * created with the RestTemplate class. Directly instantiating RestTemplate allows us to avoid
 * this behaviour.
 *
 * @author sadatmalik
 */
@Component
public class FusionBankingDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * The code that calls the banking service via the Spring Discovery Client.
     *
     * Using the injected discovery service, gets a list of all the instances of
     * the banking services. Then retrieves the get authorisation url endpoint, and
     * uses it with a standard Spring RestTemplate class to call the service.
     *
     * @return
     */
    public String getAuthorizationUrl() {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances =
                discoveryClient.getInstances("fusion-banking");

        if (instances.size() == 0)
            return null;

        String serviceUri =
                String.format("%s/get-auth-url",
                        instances.get(0).getUri().toString());

        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, String.class);

        return restExchange.getBody();
    }

    public String getAccessToken(@PathVariable String authCode) {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances =
                discoveryClient.getInstances("fusion-banking");

        if (instances.size() == 0)
            return null;

        String serviceUri =
                String.format("%s/get-user-access-token/%s",
                        instances.get(0).getUri().toString(), authCode);

        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, String.class);

        return restExchange.getBody();
    }

    public Account[] getUserAccounts() {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances =
                discoveryClient.getInstances("fusion-banking");

        if (instances.size() == 0)
            return null;

        String serviceUri =
                String.format("%s/get-user-accounts",
                        instances.get(0).getUri().toString());

        ResponseEntity<Account[]> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Account[].class);

        return restExchange.getBody();
    }
}