# Etapa 1: Construcción del proyecto con Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY analisis/pom.xml .
COPY analisis/src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución del proyecto con Java 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/analisis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
