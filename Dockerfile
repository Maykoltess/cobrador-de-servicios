# Etapa 1: Compilar y empacar la app usando Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el pom.xml y el código fuente
COPY pom.xml .
COPY src ./src

# Compilar el proyecto y generar el archivo .jar
RUN mvn clean package -DskipTests

# Etapa 2: Imagen liviana para ejecutar la aplicación
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]