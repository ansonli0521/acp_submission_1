FROM openjdk:21

EXPOSE 8080

WORKDIR /app

COPY target/acp_submission_1*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]