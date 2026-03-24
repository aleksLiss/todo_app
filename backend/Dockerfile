FROM openjdk:27-ea-3-slim
WORKDIR /app
COPY build/libs/backend-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]