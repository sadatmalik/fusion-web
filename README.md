# Fusion Web

Fusion is a microservices based web application for personal finance management and analytics.

## Installation

### Env

Set the following environment variables:

- ENCRYPT_KEY - symmetric key for encrypted configuration server properties 

### Database

### Docker

Build the docker image and run with docker compose:

```bash
> mvn clean package dockerfile:build
> docker-compose up
```

Or run the image natively with docker:

```bash
> docker run -d \
-p 8081:8081 \
--env spring.profiles.active=qa \
--env spring.config.import=configserver:http://host.docker.internal:8071 \
--env encrypt.key=${ENCRYPT_KEY} \
--env spring.datasource.url="jdbc:mysql://host.docker.internal:3308/fusion_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC" \
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx \ 
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der \
fusion/fusionweb:0.0.1-SNAPSHOT
```

Supported profiles: 
- dev | test | qa | qa2 | prd

## Microservices

- [Fusion Config](https://github.com/sadatmalik/fusion-config) - cloud configuration service
- [Fusion Discovery](https://github.com/sadatmalik/fusion-discovery) - cloud discovery service
- [Fusion Banking](https://github.com/sadatmalik/fusion-banking) - cloud banking service: provides a load balanced open-banking api client gateway implementation.

### Service Discovery and Load Balancing
Fusion-web support 3 templates for service discovery and load balancing:
- Spring Discovery Client: offers the lowest level of access to the Load Balancer and the services registered 
within it. Supports querying for all services registered with the Spring Cloud Load Balancer client and their 
corresponding URLs. Gives the developer control over how to select services and to load-balance usage.

- Spring Load Balancer-enabled Rest Template: more common mechanisms for interacting with the Load Balancer via 
Spring. Builds target URL using the Eureka (discovery) service ID, abstracting away the actual service location 
and port. Requests are automatically round-robin load-balanced amongst the registered endpoint services.

- Netflix Feign Client: defines a Java interface with Spring Cloud annotations corresponding to the Eureka-based 
service endpoints that the Spring Cloud Load Balancer will invoke. The Spring Cloud framework dynamically 
generates a proxy class to invoke the targeted REST service.

Use the configuration property (@todo not yet implemented) to select which of the load balancer templates to
invoke at runtime. Defaults to Feign Client.

## Service Health

The following endpoints are supported:

/actuator/env - service configuration