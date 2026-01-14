# Estágio de Build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copia tudo para o servidor
COPY . .

# Comando que limpa e compila forçando o padrão de texto UTF-8
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Estágio de Execução
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]