FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# docker build -t demo-bank-provider:1.0 .
# docker run -p 8083:8083 demo-bank-provider:1.0 .