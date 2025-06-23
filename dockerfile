# Etapa 1: Construcción del proyecto
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY analisis/pom.xml .
COPY analisis/src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución del proyecto
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/analisis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
