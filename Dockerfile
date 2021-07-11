FROM openjdk:11

USER root

ARG JAR_FILE=build/libs/polinsight-0.0.1.jar

# working directory
WORKDIR /app

COPY ${JAR_FILE} app.jar

LABEL title="PolInsight"

LABEL description="polinsight web project"

ENV profile="default"

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=${profile}","-jar","app.jar"]