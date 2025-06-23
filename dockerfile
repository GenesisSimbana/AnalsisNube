# Usa una imagen oficial de Java 21
FROM eclipse-temurin:21-jdk

# Crea directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR compilado
COPY target/analisis-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080 (puerto default de Spring Boot)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
