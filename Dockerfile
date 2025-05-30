# Fase 1: Costruzione del progetto
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY . .

# Rende mvnw eseguibile (se non lo è già)
RUN chmod +x mvnw

# Costruisce l'app Spring Boot (senza eseguire i test)
RUN ./mvnw clean package -DskipTests

# Fase 2: Immagine finale più leggera
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia solo il file JAR dalla build precedente
COPY --from=build /app/target/*.jar app.jar

# Avvia l'applicazione Spring Boot
CMD ["java", "-jar", "app.jar"]
