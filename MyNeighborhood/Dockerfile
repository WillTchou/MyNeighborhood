FROM openjdk:17-jdk-alpine
VOLUME tmp
EXPOSE 8080
ARG JAR_FILE=target/MyNeighborhood-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} my-neighborhood.jar
ENTRYPOINT ["java","-jar","/my-neighborhood.jar"]
