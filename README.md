# Fusion Web

Fusion is a microservice based personal finance management web application.

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
--env spring.profiles.active=dev \
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx \ 
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der \
fusion/fusionweb:0.0.1-SNAPSHOT
```

Supported profiles: 
- dev | test | qa | qa2 | prd

## Microservices

- [Fusion Config](https://github.com/sadatmalik/fusion-config) - cloud configuration service

## Service Health

The following endpoints are supported:

/actuator/env - service configuration