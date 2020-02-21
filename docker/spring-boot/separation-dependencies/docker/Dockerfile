FROM openjdk:8-jdk-alpine
ARG DEPENDENCY=dist
COPY ${DEPENDENCY}/lib /app/lib
COPY ${DEPENDENCY}/*.jar /app/app.jar
ENTRYPOINT ["java","-cp","app/app.jar:app/lib/*","com.smile.api.WebApiSampleApplication"]