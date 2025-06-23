# 🏦 Sistema de Análisis de Riesgos - API REST

Sistema de gestión de análisis de riesgos para el proceso de originación de créditos del Banco BanQuito. Esta API proporciona funcionalidades para el seguimiento del historial de estados de solicitudes y las observaciones de analistas.

## ✨ Características

- **Gestión de Historial de Estados**: Seguimiento completo del flujo de estados de las solicitudes de crédito
- **Observaciones de Analistas**: Sistema para registrar y consultar observaciones realizadas por analistas
- **API RESTful**: Endpoints bien documentados siguiendo las mejores prácticas
- **Documentación Automática**: Swagger/OpenAPI para documentación interactiva
- **Validación de Datos**: Validación robusta de entrada con mensajes de error descriptivos
- **Paginación**: Soporte para consultas paginadas en todos los endpoints
- **Logging**: Sistema de logging configurado con Logback
- **Base de Datos**: Integración con PostgreSQL

## 🛠 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **SpringDoc OpenAPI 2.0.3**
- **Lombok**
- **Logback**

## 📋 Requisitos Previos

- Java 21 
- Maven 3.6+
- PostgreSQL 12+
- Git

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/GenesisSimbana/AnalisisRiesgo.git
cd AnalisisRiesgo/analisis
```

### 2. Configurar la base de datos

Crear una base de datos PostgreSQL:

```sql
CREATE DATABASE postgres;
CREATE SCHEMA analisis_creditos;
```

### 3. Configurar variables de entorno

Editar `src/main/resources/application.properties` con tus credenciales de base de datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### 4. Compilar y ejecutar

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run
```

## ⚙️ Configuración

### Configuración de Base de Datos

El proyecto está configurado para usar PostgreSQL con las siguientes configuraciones por defecto:

- **Host**: localhost
- **Puerto**: 5432
- **Base de datos**: postgres
- **Schema**: analisis_creditos
- **Usuario**: postgres
- **Contraseña**: root

### Configuración de Logging

El sistema de logging está configurado en `src/main/resources/logback-spring.xml` con:

- Logs de aplicación en `logs/spring-boot-logger.log`
- Rotación diaria de archivos
- Nivel de log configurable por ambiente

## 📖 Uso

### Acceso a la Documentación

Una vez que la aplicación esté ejecutándose, puedes acceder a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🔌 API Endpoints

## 📊 Historial de Estados

### 🔹 Listar todos los historiales

```
GET http://localhost:8080/v1/historial-estados
```

#### 📌 Parámetros de paginación:
- `page`: Número de página (0-based, por defecto: 0)
- `size`: Tamaño de la página (por defecto: 20)
- `sortBy`: Campo para ordenar (por defecto: fechaHora)
- `sortDir`: Dirección del ordenamiento - "asc" o "desc" (por defecto: desc)

#### 📌 Ejemplo:
```
GET http://localhost:8080/v1/historial-estados?page=0&size=10&sortBy=fechaHora&sortDir=desc
```

### 🔹 Buscar por ID

```
GET http://localhost:8080/v1/historial-estados/{id}
```

### 🔹 Buscar por solicitud

```
GET http://localhost:8080/v1/historial-estados/solicitud/{idSolicitud}
```

### 🔹 Buscar por estado

```
GET http://localhost:8080/v1/historial-estados/estado/{estado}
```

#### 📌 Estados disponibles:
- `Borrador`
- `EnRevision`
- `Aprobada`
- `Rechazada`
- `Cancelada`

### 🔹 Buscar por usuario

```
GET http://localhost:8080/v1/historial-estados/usuario/{usuario}
```

### 🔹 Crear historial de estados

```
POST http://localhost:8080/v1/historial-estados
```

```json
{
  "idSolicitud": 5,
  "estado": "Rechazada",
  "usuario": "analista001",
  "motivo": "Intento de transición inválida: Aprobada"
}
```

### 🔹 Actualizar historial completo

```
PUT http://localhost:8080/v1/historial-estados/{id}
```

```json
{
  "idSolicitud": 5,
  "estado": "Aprobada",
  "usuario": "analista001",
  "motivo": "Solicitud aprobada después del análisis de riesgo"
}
```

> 📌 **Nota:** Cambiar los campos que se desean actualizar.

### 🔹 Actualizar historial parcialmente

```
PATCH http://localhost:8080/v1/historial-estados/{id}
```

```json
{
  "estado": "En Análisis",
  "motivo": "Solicitud en proceso de revisión detallada"
}
```

### 🔹 Eliminar historial

```
DELETE http://localhost:8080/v1/historial-estados/{id}
```

---

## 📝 Observaciones de Analistas

### 🔹 Listar todas las observaciones

```
GET http://localhost:8080/v1/observaciones-analistas
```

#### 📌 Parámetros de paginación:
- `page`: Número de página (0-based, por defecto: 0)
- `size`: Tamaño de la página (por defecto: 20)
- `sortBy`: Campo para ordenar (por defecto: fechaHora)
- `sortDir`: Dirección del ordenamiento - "asc" o "desc" (por defecto: desc)

#### 📌 Ejemplo:
```
GET http://localhost:8080/v1/observaciones-analistas?page=0&size=15&sortBy=fechaHora&sortDir=desc
```

### 🔹 Buscar por ID

```
GET http://localhost:8080/v1/observaciones-analistas/{id}
```

### 🔹 Buscar por solicitud

```
GET http://localhost:8080/v1/observaciones-analistas/solicitud/{idSolicitud}
```

### 🔹 Buscar por usuario

```
GET http://localhost:8080/v1/observaciones-analistas/usuario/{usuario}
```

### 🔹 Crear observación de analista

```
POST http://localhost:8080/v1/observaciones-analistas
```

```json
{
  "idSolicitud": 3,
  "usuario": "administrador@banquito.com",
  "razonIntervencion": "Agregar observación durante el proceso de revisión"
}
```

### 🔹 Actualizar observación completa

```
PUT http://localhost:8080/v1/observaciones-analistas/{id}
```

```json
{
  "idSolicitud": 3,
  "usuario": "administrador@banquito.com",
  "razonIntervencion": "Observación actualizada con información adicional del análisis"
}
```

> 📌 **Nota:** Cambiar los campos que se desean actualizar.

### 🔹 Actualizar observación parcialmente

```
PATCH http://localhost:8080/v1/observaciones-analistas/{id}
```

```json
{
  "razonIntervencion": "Observación modificada con nuevos hallazgos del análisis"
}
```

### 🔹 Eliminar observación

```
DELETE http://localhost:8080/v1/observaciones-analistas/{id}
```

---

## 📚 Documentación de la API

La documentación completa de la API está disponible a través de Swagger UI en:

**http://localhost:8080/swagger-ui.html**

Esta interfaz permite:

- Explorar todos los endpoints disponibles
- Probar las operaciones directamente desde el navegador
- Ver los modelos de datos (DTOs)
- Consultar códigos de respuesta y descripciones

## 📁 Estructura del Proyecto

```
src/main/java/com/banquito/originacion/analisis/
├── AnalisisApplication.java              # Clase principal de Spring Boot
├── config/                               # Configuraciones
│   ├── OpenApiConfig.java               # Configuración de Swagger/OpenAPI
│   └── WebConfig.java                   # Configuración web
├── controller/                          # Controladores REST
│   ├── dto/                            # Data Transfer Objects
│   │   ├── HistorialEstadosDTO.java
│   │   └── ObservacionAnalistasDTO.java
│   ├── mapper/                         # Mappers entre entidades y DTOs
│   │   ├── HistorialEstadosMapper.java
│   │   └── ObservacionAnalistasMapper.java
│   ├── HistorialEstadosController.java
│   └── ObservacionAnalistasController.java
├── enums/                              # Enumeraciones
│   └── EstadoHistorialEnum.java
├── exception/                          # Excepciones personalizadas
│   ├── HistorialEstadosNotFoundException.java
│   ├── InvalidTransitionException.java
│   └── ObservacionAnalistasNotFoundException.java
├── model/                              # Entidades JPA
│   ├── HistorialEstados.java
│   └── ObservacionAnalistas.java
├── repository/                         # Repositorios JPA
│   ├── HistorialEstadosRepository.java
│   └── ObservacionAnalistasRepository.java
└── service/                           # Lógica de negocio
    ├── HistorialEstadosService.java
    └── ObservacionAnalistasService.java
```

## 📝 Logging

El sistema utiliza Logback para el logging con las siguientes características:

- **Archivo de logs**: `logs/spring-boot-logger.log`
- **Rotación**: Diaria con archivos comprimidos
- **Niveles**: Configurables por ambiente
- **Formato**: Incluye timestamp, nivel, clase y mensaje

### Configuración de Logs

Los logs se configuran en `src/main/resources/logback-spring.xml` y pueden ser personalizados según las necesidades del ambiente.

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request