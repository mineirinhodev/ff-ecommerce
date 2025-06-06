# 🛠️ Auth Service — Backend com Spring Boot + AWS + Terraform

## 🚀 Descrição do Projeto
Este projeto é uma API de autenticação robusta, escalável e pronta para produção.  
Utiliza **Spring Boot**, **AWS Cognito** para gerenciamento de autenticação, além de ser totalmente provisionado na **AWS via Terraform**, rodando no **ECS com Fargate**, e exposto via **API Gateway** com suporte a **HTTPS** e **JWT**.

---

## 🌐 Arquitetura
Usuário → API Gateway (HTTPS + JWT Cognito)
↓
ECS Fargate (Spring Boot)
↓
AWS Cognito (Auth)
↓
DynamoDB (Terraform Lock) + S3 (State)

## ⚙️ Tecnologias e Serviços Utilizados
🧠 Spring Boot 3 + Java 21

🔐 AWS Cognito (Auth + JWT)

🐳 ECS (Fargate) — Containers serverless

📦 ECR — Repositório de imagens Docker

🌐 API Gateway HTTP API

🔧 Terraform + Terragrunt — Infra como código

🔥 GitHub Actions — CI/CD

🔐 IAM Roles e Policies

☁️ S3 + DynamoDB — Backend remoto do Terraform

📈 CloudWatch — Logs e monitoramento

## 📁 Estrutura do Projeto
```plaintext
backend/
├── src/
├── Dockerfile
├── pom.xml
└── README.md

infra/
├── modules/
│   ├── vpc/
│   ├── ecs/
│   ├── ecr/
│   ├── api-gateway/
│   └── security/
├── live/
│   ├── dev/
│   └── prod/
└── terragrunt.hcl
```
---

## 🔥 Funcionalidades

- ✅ Registro de usuário (via AWS Cognito)
- ✅ Login com retorno de JWT (Access Token + ID Token + Refresh Token)
- ✅ Recuperação e redefinição de senha
- ✅ Refresh Token
- ✅ Autorização via grupos Cognito (`cognito:groups`)
- ✅ API Gateway com HTTPS
- ✅ Deploy automático via CI/CD

---

## 🏗️ Deploy da Infraestrutura

### ✔️ Pré-requisitos:

- AWS CLI configurado
- Terraform instalado
- Terragrunt instalado
- Docker instalado (para build local)

### ✔️ Passos:

```bash
cd infra/live/dev

# Inicializar
terragrunt init

# Visualizar mudanças
terragrunt plan

# Aplicar infraestrutura
terragrunt apply
```


## 🐳 Build e Deploy da Imagem Docker
```bash
# Build local da imagem
docker build -t auth-service .

# Login no ECR
aws ecr get-login-password --region us-east-1 | \
docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Push da imagem
docker tag auth-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/auth-service:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/auth-service:latest
```

## 🚀 Pipeline GitHub Actions
✅ Build da imagem Docker

✅ Push para AWS ECR

✅ Atualização da Task Definition no ECS

✅ Deploy automático no ECS

✔️ O pipeline roda automaticamente a cada push no branch main.

## 🔗 Endpoints da API
Método	Endpoint	Descrição
| Método | Endpoint                             | Descrição                    |
|--------|---------------------------------------|------------------------------|
| POST   | `/api/auth/register`                 | Registrar usuário            |
| POST   | `/api/auth/login`                    | Login e geração de tokens    |
| POST   | `/api/auth/forgot-password`          | Esqueci minha senha          |
| POST   | `/api/auth/confirm-forgot-password`  | Confirmar nova senha         |
| GET    | `/api/user/profile`                  | (Protegido) Ver perfil       |

## 🔐 Autenticação JWT Cognito
Após realizar o login e obter o accessToken, envie nas requisições protegidas no header HTTP assim:
```http
Authorization: Bearer {accessToken}
```

## 🌎 API Gateway URL
```
https://{api-id}.execute-api.us-east-1.amazonaws.com
✔️ Esse endpoint pode ser substituído por um domínio próprio via Route 53 + ACM.

```
## 📜 Variáveis Sensíveis (GitHub Secrets)

| Nome                         | Descrição                         |
|------------------------------|------------------------------------|
| `AWS_ACCESS_KEY_ID`          | Chave da AWS                      |
| `AWS_SECRET_ACCESS_KEY`      | Secret da AWS                     |
| `AWS_ACCOUNT_ID`             | ID da conta AWS                   |
| `AWS_REGION`                 | Região AWS (ex.: `us-east-1`)     |
| `ECR_REPOSITORY`             | Nome do repositório no ECR        |
| `ECS_CLUSTER_NAME`           | Nome do Cluster ECS               |
| `ECS_SERVICE_NAME`           | Nome do Service ECS               |
| `ECS_TASK_DEFINITION_FAMILY` | Nome da Task Definition           |

## Deploy e Destroy automatizados com GitHub Actions

Este projeto utiliza workflows do GitHub Actions para automatizar o deploy e o destroy da infraestrutura Terraform.

### Como acionar o deploy
- Faça um commit na branch `main` com a mensagem contendo `#deploy` (exemplo: `feat: nova feature #deploy`).
- O workflow de deploy será executado automaticamente.
- Também é possível rodar manualmente pela aba **Actions** do GitHub, selecionando o workflow "Terraform Deploy" e clicando em **Run workflow**.

### Como acionar o destroy
- Faça um commit na branch `main` com a mensagem contendo `#destroy` (exemplo: `chore: limpeza de ambiente #destroy`).
- O workflow de destroy será executado automaticamente.
- Também é possível rodar manualmente pela aba **Actions** do GitHub, selecionando o workflow "Terraform Destroy" e clicando em **Run workflow**.

### Observações
- Os módulos `router53-acm` e `api-gateway-domain` não são aplicados nem destruídos automaticamente para evitar custos extras com Route 53.
- Certifique-se de que os secrets da AWS estejam configurados em **Settings > Secrets and variables > Actions**.
- Consulte os arquivos `.github/workflows/terraform-deploy.yml` e `.github/workflows/terraform-destroy.yml` para detalhes e ordem dos módulos.

```
✔️ Isso deleta toda a infraestrutura.


##  🚀 Melhorias Futuras
✔️ Implementação de domínio customizado (Route 53 + ACM)

✔️ Load Balancer na frente do ECS

✔️ Monitoramento avançado com CloudWatch + SNS

✔️ Rate limiting e proteção extra no API Gateway

✔️ Gerenciamento de ambientes multi-account (via Organizations)

## 🤝 Contribuição
Sinta-se livre para abrir PRs, issues ou propor melhorias.

## 📜 Licença
Este projeto está licenciado sob a MIT License.

## 👨‍💻 Desenvolvido por
Emerson Alves — Backend Engineer

☁️ Desenvolvedor em AWS, Terraform, Spring Boot e Arquitetura Cloud.
