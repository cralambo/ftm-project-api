# ============================================================
# STAGE 1 : BUILD
# JDK 25 + Maven
# ============================================================
FROM maven:3.9.16-eclipse-temurin-25 AS builder

WORKDIR /app

# Copier le pom.xml en premier pour profiter du cache Docker
COPY pom.xml .

# Télécharger les dépendances Maven
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et générer le JAR
RUN mvn clean package -DskipTests


# ============================================================
# STAGE 2 : RUNTIME
# JDK 25 / JRE
# ============================================================
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copier le JAR généré depuis le stage de build
COPY --from=builder /app/target/*.jar app.jar

# Port Spring Boot
EXPOSE 8095

# Configuration JVM
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Démarrer Spring Boot
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]