FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# Compila todos los .java en src usando los JARs que encuentre en el proyecto
RUN javac -cp ".:lib/*:.idea/libraries/*" src/*.java -d bin/ || javac -cp "." src/*.java -d bin/

EXPOSE 8080

CMD ["java", "-cp", "bin:lib/*:.idea/libraries/*", "servidor"]