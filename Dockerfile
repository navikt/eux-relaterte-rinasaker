FROM navikt/java:17

ADD target/eux-relaterte-rinasaker.jar /app/app.jar
COPY .nais/init-scripts /init-scripts
