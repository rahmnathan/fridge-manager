FROM adoptopenjdk/openjdk11:jre-alpine

RUN groupadd fridge-manager && useradd fridge-manager -g fridge-manager && mkdir -p /opt/fridge-manager/config

ARG JAR_FILE
ADD target/$JAR_FILE /opt/fridge-manager/fridge-manager.jar

RUN chown -R fridge-manager:fridge-manager /opt/fridge-manager

USER fridge-manager

WORKDIR /opt/fridge-manager/
ENTRYPOINT java -jar fridge-manager.jar