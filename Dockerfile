# Imagen base con Java 21
FROM eclipse-temurin:21-jdk

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el .jar al contenedor
COPY target/sistemaventas-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto por la app
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
