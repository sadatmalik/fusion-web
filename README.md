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
## Service Health

The following endpoints are supported:

/actuator/env - service configuration