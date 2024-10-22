FROM gcr.io/distroless/java21
COPY eux-relaterte-rinasaker-webapp/target/eux-relaterte-rinasaker.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
