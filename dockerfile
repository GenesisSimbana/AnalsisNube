# Usa una imagen oficial de Java 21
FROM eclipse-temurin:21-jdk

# Crea una carpeta dentro del contenedor
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY target/*.jar app.jar

# Expón el puerto (opcional si usas 8080)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
