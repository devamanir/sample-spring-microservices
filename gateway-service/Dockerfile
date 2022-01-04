FROM openjdk:8-jdk-alpine
MAINTAINER Devamani Ramaswamy <umaildeva@gmail.com>
ADD target/gateway-service.jar gateway-service.jar
EXPOSE 8765
ENTRYPOINT ["java", "-jar", "/gateway-service.jar"]
