# Imagen base
FROM eclipse-temurin:21-jdk

# Instalar Python y pip
RUN apt-get update && apt-get install -y python3 python3-pip

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos necesarios
COPY target/sistemaventas-0.0.1-SNAPSHOT.jar app.jar
COPY modeladoLinox /app/modeladoLinox
COPY requirements.txt .

# Instalar dependencias Python
RUN pip3 install --no-cache-dir --break-system-packages -r requirements.txt

# Exponer puertos
EXPOSE 8080 5000

# Comando para iniciar Flask en segundo plano y luego Spring Boot
CMD python3 modeladoLinox/api_prediccion.py & java -jar app.jar
