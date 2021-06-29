FROM openjdk:11

USER root

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

LABEL title="PolInsight"

LABEL version="0.1"

LABEL description="polinsight web project"

# default profile = prod
ENV profile="prod"

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=${profile}","-jar","app.jar"]