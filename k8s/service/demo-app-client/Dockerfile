FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# docker build -t demo-app-client:1.0 .
# docker run -p 8081:8081 demo-app-client:1.0 .