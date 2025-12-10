FROM jelastic/maven:3.9.5-openjdk-21 AS build

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package

# Execution Stage
FROM eclipse-temurin:21-jre
#FROM khipu/openjdk17-alpine:latest

WORKDIR /africoin

COPY --from=build /home/app/target/africoin-service.jar .

ENV TZ=Africa/Lagos

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8002

ENTRYPOINT [ "sh", "-c", "java -jar /africoin/africoin-service.jar --spring.config.additional-location=$CONFIG_LOCATION"]



