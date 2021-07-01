FROM openjdk:11

USER root

# working directory
WORKDIR /app

COPY . .

ARG version=

ARG JAR_FILE=build/libs/polinsight-${version}.jar

COPY ${JAR_FILE} app.jar

LABEL title="PolInsight"

LABEL version="${version}"

LABEL description="polinsight web project"

# default profile = prod
ENV profile="default"

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","app.jar"]