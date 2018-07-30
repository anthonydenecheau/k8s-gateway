FROM openjdk:8-jdk-alpine
MAINTAINER Anthony Denecheau <anthony.denecheau@gmail.com>
RUN apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/gateway
ADD @project.build.finalName@.jar /usr/local/gateway/

ENV JAVA_OPTS=${JAVA_OPTS}
ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SERVER_PORT=${SERVER_PORT}

ENTRYPOINT exec java $JAVA_OPTS \
 -Dserver.port=$SERVER_PORT \
 -Djava.security.egd=file:/dev/./urandom \
 -Dspring.profiles.active=$SPRING_PROFILE \
 -jar /usr/local/gateway/@project.build.finalName@.jar
 
