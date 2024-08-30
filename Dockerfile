# Используем официальный образ Java JDK в качестве базового
FROM openjdk:21
WORKDIR /app
COPY target/BookLibrary-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]