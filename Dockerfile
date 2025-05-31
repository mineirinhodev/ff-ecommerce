# 🔥 Usa imagem com Java 21 LTS
FROM eclipse-temurin:21-jdk-jammy

# 🔧 Cria diretório de trabalho dentro do container
WORKDIR /app

# 🚀 Copia o JAR gerado para dentro do container
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# 🔥 Expõe a porta do container
EXPOSE 8080

# 🎯 Comando que roda sua aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]