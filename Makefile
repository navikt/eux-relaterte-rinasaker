package:
	mvnd clean install

run-jar:
	java -jar eux-relaterte-rinasaker-webapp/target/eux-relaterte-rinasaker.jar

run: package run-jar
