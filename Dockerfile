FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD /build/libs/api-gateway-0.0.2.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]