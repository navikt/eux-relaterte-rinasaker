FROM navikt/java:17

ADD eux-relaterte-rinasaker-webapp/target/eux-relaterte-rinasaker.jar /app/app.jar
COPY .nais/init-scripts /init-scripts
