MAINTAINER brMonteiro-G

FROM openjdk:17-jdk-alpine AS build

WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package

FROM openjdk:17-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=jwt-validator.jar

WORKDIR /opt/app
COPY --from=build /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","/jwt-validator.jar", "-Dspring.profiles.active=local"]