FROM openjdk:11

USER root

ARG JAR_FILE=./build/libs/app.jar

# working directory
WORKDIR /app

RUN mkdir "upload_dir"

RUN mkdir "logs"

COPY ${JAR_FILE} app.jar

LABEL title="PolInsight"

LABEL description="polinsight web project"

ENV profile="prod"

#ENV maria_host="pol-mariadb"
#
#ENV mongo_host="pol-mongodb"
#
#ENV redis_host="pol-redis"

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=${profile}","-jar", "app.jar"]