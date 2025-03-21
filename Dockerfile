# Build Angular
FROM node:23 AS build_angular

WORKDIR /src

RUN npm i -g @angular/cli

COPY client/src src
COPY client/*.json .
COPY client/dist dist

RUN npm ci && ng build

# Build spring boot
FROM openjdk:23-jdk AS builder_java

WORKDIR /src

COPY server/mvnw .
COPY server/pom.xml .
COPY server/src src
COPY server/.mvn .mvn

COPY --from=build_angular /src/dist/client/browser/* src/main/resources/static

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true

FROM openjdk:23-jdk 

WORKDIR /app

COPY --from=builder_java /src/target/server-0.0.1-SNAPSHOT.jar app.jar

# Set Env Variables
ENV PORT=8080

EXPOSE ${PORT}
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar


