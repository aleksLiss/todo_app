FROM openjdk:27-ea-3-slim
WORKDIR /app
COPY build/libs/email_sender-0.0.1-SNAPSHOT.jar email_sender.jar
ENTRYPOINT ["java", "-jar", "email_sender.jar"]