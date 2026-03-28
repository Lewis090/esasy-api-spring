# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /build

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código-fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiar JAR do stage anterior
COPY --from=builder /build/target/*.jar app.jar

# Variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
