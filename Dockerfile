# 🔥 Usa imagem com Java 21 LTS
FROM amazoncorretto:21-alpine-jdk

# 🔧 Cria diretório de trabalho dentro do container
WORKDIR /app

# 🚀 Copia o JAR gerado para dentro do container
COPY target/auth-service.jar app.jar

# 🔥 Expõe a porta do container
EXPOSE 8080

# 🎯 Comando que roda sua aplicação
ENTRYPOINT ["java","-jar","/app.jar"]
