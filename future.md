# Auth Service

Serviço de autenticação construído com Spring Boot e integrado com diversos serviços AWS.

## 🚀 Funcionalidades Principais

### 1. Autenticação com AWS Cognito
- Registro de usuários
- Login com JWT
- Recuperação de senha
- Confirmação de email
- Gerenciamento de perfil

### 2. Serviço de Email (Amazon SES)
- Envio de emails de boas-vindas
- Notificações de segurança
- Templates HTML personalizados
- Rastreamento de entrega

### 3. Armazenamento (Amazon S3)
- Upload de fotos de perfil
- Armazenamento de documentos
- URLs pré-assinadas
- Gerenciamento de arquivos

### 4. Auditoria (DynamoDB)
- Registro de tentativas de login
- Histórico de alterações de senha
- Atualizações de perfil
- Rastreamento de atividades

### 5. Monitoramento (CloudWatch)
- Métricas de uso
- Logs de aplicação
- Alertas configuráveis
- Dashboard de monitoramento

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.2.5
- Spring Security
- AWS SDK 2.25.23
- Swagger/OpenAPI
- Lombok

## 📦 Dependências AWS

```xml
<aws.sdk.version>2.25.23</aws.sdk.version>

<!-- AWS Cognito -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>cognitoidentityprovider</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>

<!-- Amazon SES -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>ses</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>

<!-- Amazon S3 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>

<!-- DynamoDB -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>dynamodb</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>

<!-- CloudWatch -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>cloudwatch</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>
```

## ⚙️ Configuração

### Variáveis de Ambiente Necessárias

```properties
# AWS Credentials
aws.region=
aws.access-key-id=
aws.secret-key=

# Cognito
aws.cognito.user-pool-id=
aws.cognito.client-id=

# SES
aws.ses.from-email=

# S3
aws.s3.bucket-name=
```

## 📚 Documentação API

A documentação da API está disponível através do Swagger UI em:
```
http://localhost:8080/swagger-ui.html
```

## 🔧 Instalação e Execução

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/auth-service.git
```

2. Configure as variáveis de ambiente

3. Execute o projeto
```bash
mvn spring-boot:run
```

## 📊 Monitoramento

### Métricas Disponíveis
- Tentativas de login (sucesso/falha)
- Latência da API
- Usuários concorrentes
- Tentativas de redefinição de senha

### Alertas Configurados
- Alta taxa de falhas de login
- Latência elevada
- Picos de uso
- Erros críticos

## 🔐 Segurança

- Autenticação JWT
- Proteção contra ataques de força bruta
- Rate limiting
- Logs de auditoria
- Monitoramento de atividades suspeitas

## 📝 Logs e Auditoria

### Eventos Registrados
- Login/Logout
- Alterações de senha
- Atualizações de perfil
- Upload de arquivos
- Envio de emails

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.