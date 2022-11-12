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

VOLUME /etc/app

WORKDIR /opt/app

EXPOSE 8080
COPY --from=builder /opt/app/build/libs/ /opt/app/

ENTRYPOINT ["java", "-jar", "/opt/app/api-gateway.jar"]