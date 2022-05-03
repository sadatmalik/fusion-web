package com.sadatmalik.fusionweb.services.client;

import com.sadatmalik.fusionweb.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * An alternative to the Spring Load Balancer–enabled RestTemplate class is Netflix’s Feign
 * client library.
 *
 * The Feign library takes a different approach to calling a REST service. With this approach,
 * we first define a Java interface and then add Spring Cloud annotations to map what
 * Eureka-based service the Spring Cloud Load Balancer will invoke.
 *
 * The Spring Cloud framework will dynamically generate a proxy class to invoke the targeted
 * REST service. There’s no code written for calling the service other than an interface
 * definition.
 *
 * To enable the Feign client for use in our licensing service, we need to add a new annotation,
 * &#064;EnableFeignClients,  to the application class FusionWebPrototypeApplication.
 *
 * The interface annotation @FeignClient identifies the service to feign.
 *
 * To use the FusionBankingFeignClient class, all we need to do is to autowire it and use it. The
 * Feign client code takes care of all the coding for us.
 *
 * A note on error handling:
 * When we use the standard Spring RestTemplate class, all service call HTTP status codes are
 * returned via the ResponseEntity class’s getStatusCode() method. With the Feign client, any
 * HTTP 4xx–5xx status codes returned by the service being called are mapped to a FeignException.
 * The FeignException contains a JSON body that can be parsed for the specific error
 * message.
 *
 * Feign provides the ability to write an error decoder class that will map the error back to a
 * custom Exception class:
 *
 * @see <a href="https://github.com/Netflix/feign/wiki/Custom-error-handling">...</a>
 *
 * @author sadatmalik
 */
@FeignClient("fusion-banking")
public interface FusionBankingFeignClient {

    /**
     * Defines the path and action to the endpoint and the parameters passed into the
     * endpoint. This method can be called by the client to invoke the fusion-banking
     * service. The endpoint method is defined exactly as if exposing an endpoint in a
     * Spring controller class.
     *
     * @return The return value from the call to the banking service is automatically
     * mapped to the String class that’s defined as the return value for the method.
     */
    @RequestMapping(
            method= RequestMethod.GET,
            value="/get-auth-url",
            consumes="application/json")
    String getAuthorizationUrl();

    @RequestMapping(
            method= RequestMethod.GET,
            value="/get-user-access-token/{authCode}",
            consumes="application/json")
    String getAccessToken(@PathVariable("authCode") String authCode);

    @RequestMapping(
            method= RequestMethod.GET,
            value="/get-user-accounts",
            consumes="application/json")
    Account[] getUserAccounts();
}
