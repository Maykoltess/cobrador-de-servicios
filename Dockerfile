FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN javac servidor.java

EXPOSE 8080

CMD ["java", "servidor"]