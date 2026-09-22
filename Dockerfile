FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/app

COPY target/*.jar /opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

CMD java -jar application.jar

EXPOSE 8080