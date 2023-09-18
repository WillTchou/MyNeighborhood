FROM maven:3.8.6 AS build
COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/my-neighborhood-docker.jar
COPY --from=build /usr/src/app/${JAR_FILE} /usr/src/app/my-neighborhood.jar
ENV PORT 8080
ENTRYPOINT ["java","-jar","/usr/src/app/my-neighborhood.jar"]
EXPOSE 8080