FROM openjdk:17-jdk-alpine AS build

ARG ACCESS_KEY
ENV ACCESS_KEY=$ACCESS_KEY

ARG SECRET_KEY
ENV SECRET_KEY=$SECRET_KEY

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17-jdk-slim
WORKDIR app
COPY --from=build target/*.jar jwt-validator.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","jwt-validator.jar", "-Dspring.profiles.active=prod"]
