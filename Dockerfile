FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

EXPOSE 8080

CMD ["java", "-cp", "out/production/cobrador de servicios:.idea/libraries/*:lib/*", "cobrador.servidor"]