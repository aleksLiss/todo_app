FROM openjdk:27-ea-3-slim
WORKDIR /app
COPY build/libs/todo_app-0.0.1-SNAPSHOT.jar todo_app.jar
ENTRYPOINT ["java", "-jar", "todo_app.jar"]