FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Compila los archivos del paquete cobrador y crea el directorio de salida
RUN mkdir -p bin && javac -cp "out/production/cobrador de servicios:lib/*" -d bin src/cobrador/*.java || mkdir -p bin && javac -d bin src/cobrador/*.java

EXPOSE 8080

CMD ["java", "-cp", "bin:out/production/cobrador de servicios:lib/*", "cobrador.servidor"]