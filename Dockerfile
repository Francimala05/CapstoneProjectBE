# Stage 1: build dell'app
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copia i file necessari per Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copia il resto del progetto
COPY pom.xml .
COPY src src

# Rendi eseguibile lo script mvnw
RUN chmod +x mvnw

# Builda il progetto senza test
RUN ./mvnw clean package -DskipTests

# Stage 2: immagine finale più leggera
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia il jar dal build
COPY --from=build /app/target/*.jar app.jar

# Esponi la porta (opzionale ma consigliato)
EXPOSE 8080

# Comando per partire
ENTRYPOINT ["java", "-jar", "app.jar"]
