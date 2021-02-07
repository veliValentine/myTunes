FROM openjdk:15
VOLUME tmp
ADD target/demo-0.0.1-SNAPSHOT.jar velivalentine-mytunes.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]