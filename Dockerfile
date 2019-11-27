FROM adoptopenjdk/openjdk11:alpine-jre

RUN addgroup -S fridge-manager && adduser -S fridge-manager -G fridge-manager && mkdir -p /opt/fridge-manager/config

ARG JAR_FILE
ADD target/$JAR_FILE /opt/fridge-manager/fridge-manager.jar

RUN chown -R fridge-manager:fridge-manager /opt/fridge-manager

USER fridge-manager

WORKDIR /opt/fridge-manager/
ENTRYPOINT java -jar fridge-manager.jar