FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac src/servidor.java || javac servidor.java

EXPOSE 8080

CMD ["java", "-cp", "src:.", "servidor"]