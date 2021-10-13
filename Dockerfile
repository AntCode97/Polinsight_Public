FROM openjdk:11

USER root

ARG JAR_FILE=./build/libs/app.jar

# working directory
WORKDIR /app

RUN mkdir -p "/app/data/image"  \
    "/app/data/files" \
    "/app/data/thumbnail" \
    "/app/logs" \
    "/app/data/db"

COPY ${JAR_FILE} app.jar

LABEL title="PolInsight"

LABEL description="polinsight web project"

ENV profile="prod"

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=${profile}","-jar", "app.jar"]