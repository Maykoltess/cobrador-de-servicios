FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia todos los archivos del repositorio
COPY . .

# Crea la carpeta bin y compila los archivos dentro de src/cobrador/
RUN mkdir -p bin && javac -cp ".idea/libraries/*:lib/*" -d bin src/cobrador/*.java

EXPOSE 8080

# Ejecuta la clase principal del paquete
CMD ["java", "-cp", "bin:.idea/libraries/*:lib/*", "cobrador.servidor"]