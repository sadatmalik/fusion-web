# Fusion Web

Fusion is a microservice based personal finance management web application.

## Build & Run

Use the following command to build a docker image.

```bash
mvn clean package dockerfile:build
```

Run the Docker image using the following docker command:

```bash
docker run -d \
-p 8081:8081 \
--env spring.profiles.active=dev \
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/qwac.pfx \ 
--mount type=bind,source=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der,target=/Users/sadatmalik/Desktop/java-projects/Fusion/certs/server_pkcs8_key.der \
fusion/fusionweb:0.0.1-SNAPSHOT
```

Or you may start the services using docker compose:

```bash
docker-compose up
```

The following profiles are supported: dev, test, qa, qa2, prd. See Profiles for further details.

## Database Installation


## Microservices

- [Fusion Config](https://github.com/sadatmalik/fusion-config) - cloud configuration service
