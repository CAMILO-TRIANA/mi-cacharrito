# Etapa 1: Compilación con Maven y JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el archivo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el archivo JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen liviana para ejecutar la aplicación
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el ejecutable generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto de Spring Boot
EXPOSE 8081

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]