FROM gradle as builder

RUN mkdir /opt/app
WORKDIR /opt/app

COPY --chown=gradle:gradle . /opt/app

RUN gradle bootJar --no-daemon

FROM adoptopenjdk/openjdk11

LABEL org.opencontainers.image.source=https://github.com/soleo/api-gateway
LABEL org.opencontainers.image.description="API Gateway"
LABEL org.opencontainers.image.licenses=MIT

RUN mkdir /opt/app
RUN mkdir /etc/app

COPY --from=builder /opt/app/build/resources/main/ /etc/app/
VOLUME /etc/app

WORKDIR /opt/app

EXPOSE 8080
EXPOSE 8930

COPY --from=builder /opt/app/build/libs/ /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/api-gateway-0.0.8.jar", "--spring.config.location=file:/etc/app/application.yml"]