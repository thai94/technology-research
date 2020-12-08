FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# docker build -t demo-transfer-core:1.0 .
# docker run -p 8082:8082 demo-transfer-core:1.0 .