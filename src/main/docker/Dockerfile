FROM openjdk:8-jdk-alpine
MAINTAINER Anthony Denecheau <anthony.denecheau@gmail.com>
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/gateway
ADD @project.build.finalName@.jar /usr/local/gateway/

ENV JAVA_OPTS="-Duser.timezone=Europe/Paris"
ENV SPRING_PROFILE="default"
ENV SERVER_PORT="8080"

ENTRYPOINT exec java $JAVA_OPTS \
 -Dserver.port=$SERVER_PORT \
 -Djava.security.egd=file:/dev/./urandom \
 -Dspring.profiles.active=$SPRING_PROFILE \
 -jar /usr/local/gateway/@project.build.finalName@.jar
 
