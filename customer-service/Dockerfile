FROM openjdk:8-jdk-alpine
MAINTAINER Devamani Ramaswamy <umaildeva@gmail.com>
ADD target/customer-service.jar customer-service.jar
EXPOSE 3333
ENTRYPOINT ["java", "-jar", "/customer-service.jar"]
