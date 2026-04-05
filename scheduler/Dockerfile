FROM openjdk:27-ea-3-slim
WORKDIR /app
COPY build/libs/scheduler-0.0.1-SNAPSHOT.jar sheduler.jar
ENTRYPOINT ["java", "-jar", "sheduler.jar"]