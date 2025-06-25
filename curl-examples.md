# Exemplos de Curl para Auth Service

## Configuração Base
```bash
# URL base do serviço
BASE_URL="http://localhost:8080/api"

# Headers comuns
CONTENT_TYPE="Content-Type: application/json"
ACCEPT_JSON="Accept: application/json"
ACCEPT_YAML="Accept: application/yaml"
```

## Documentação da API

### 1. Swagger UI
```bash
# Acessar a interface Swagger
curl -X GET "${BASE_URL}/swagger-ui.html"

# Obter a especificação OpenAPI em JSON
curl -X GET "${BASE_URL}/v3/api-docs" \
  -H "${ACCEPT_JSON}"

# Obter a especificação OpenAPI em YAML
curl -X GET "${BASE_URL}/v3/api-docs.yaml" \
  -H "${ACCEPT_YAML}"
```

## Monitoramento (Actuator)

### 1. Health Check
```bash
# Verificar saúde do serviço
curl -X GET "${BASE_URL}/actuator/health"

# Verificar saúde detalhada
curl -X GET "${BASE_URL}/actuator/health" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Verificar saúde de componentes específicos
curl -X GET "${BASE_URL}/actuator/health/cognito"
curl -X GET "${BASE_URL}/actuator/health/db"
curl -X GET "${BASE_URL}/actuator/health/diskSpace"
```

### 2. Métricas
```bash
# Listar todas as métricas disponíveis
curl -X GET "${BASE_URL}/actuator/metrics"

# Ver métricas específicas
curl -X GET "${BASE_URL}/actuator/metrics/http.server.requests"
curl -X GET "${BASE_URL}/actuator/metrics/system.cpu.usage"
curl -X GET "${BASE_URL}/actuator/metrics/jvm.memory.used"

# Ver métricas de rate limiting
curl -X GET "${BASE_URL}/actuator/metrics/resilience4j.ratelimiter.available.permissions"
curl -X GET "${BASE_URL}/actuator/metrics/resilience4j.ratelimiter.waiting.threads"

# Ver métricas de circuit breaker
curl -X GET "${BASE_URL}/actuator/metrics/resilience4j.circuitbreaker.state"
curl -X GET "${BASE_URL}/actuator/metrics/resilience4j.circuitbreaker.failure.rate"
```

### 3. Circuit Breaker Status
```bash
# Ver status dos circuit breakers
curl -X GET "${BASE_URL}/actuator/circuitbreakers"

# Ver eventos do circuit breaker
curl -X GET "${BASE_URL}/actuator/circuitbreakerevents"

# Ver eventos de um circuit breaker específico
curl -X GET "${BASE_URL}/actuator/circuitbreakerevents/default"
```

### 4. Rate Limiter Status
```bash
# Ver status dos rate limiters
curl -X GET "${BASE_URL}/actuator/ratelimiters"

# Ver eventos do rate limiter
curl -X GET "${BASE_URL}/actuator/ratelimiterevents"

# Ver eventos de um rate limiter específico
curl -X GET "${BASE_URL}/actuator/ratelimiterevents/default"
```

## Autenticação

### 1. Registro de Usuário (Casos Detalhados)
```bash
# Registro básico
curl -X POST "${BASE_URL}/auth/register" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "name": "Usuário Exemplo"
  }'

# Registro com telefone
curl -X POST "${BASE_URL}/auth/register" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "name": "Usuário Exemplo",
    "phoneNumber": "+5511999999999"
  }'

# Registro com atributos customizados
curl -X POST "${BASE_URL}/auth/register" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "name": "Usuário Exemplo",
    "customAttributes": {
      "company": "Empresa Exemplo",
      "department": "TI",
      "role": "Desenvolvedor"
    }
  }'

# Registro com confirmação automática
curl -X POST "${BASE_URL}/auth/register" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "name": "Usuário Exemplo",
    "autoConfirm": true
  }'
```

### 2. Login (Casos Detalhados)
```bash
# Login básico
curl -X POST "${BASE_URL}/auth/login" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123"
  }'

# Login com MFA
curl -X POST "${BASE_URL}/auth/login" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "mfaCode": "123456"
  }'

# Login com device ID
curl -X POST "${BASE_URL}/auth/login" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "deviceId": "device-123"
  }'

# Login com scope personalizado
curl -X POST "${BASE_URL}/auth/login" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "password": "Senha@123",
    "scope": "api.read api.write"
  }'
```

### 3. Gerenciamento de Tokens
```bash
# Refresh token
curl -X POST "${BASE_URL}/auth/refresh-token" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "refreshToken": "seu-refresh-token-aqui"
  }'

# Revogar token
curl -X POST "${BASE_URL}/auth/revoke-token" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "token": "seu-token-aqui"
  }'

# Revogar todos os tokens do usuário
curl -X POST "${BASE_URL}/auth/revoke-all-tokens" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Listar tokens ativos
curl -X GET "${BASE_URL}/auth/tokens" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

### 4. Gerenciamento de Senha
```bash
# Solicitar reset de senha
curl -X POST "${BASE_URL}/auth/forgot-password" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com"
  }'

# Confirmar nova senha
curl -X POST "${BASE_URL}/auth/confirm-forgot-password" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "email": "usuario@exemplo.com",
    "confirmationCode": "123456",
    "newPassword": "NovaSenha@123"
  }'

# Alterar senha (usuário logado)
curl -X POST "${BASE_URL}/auth/change-password" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "oldPassword": "SenhaAtual@123",
    "newPassword": "NovaSenha@123"
  }'

# Verificar força da senha
curl -X POST "${BASE_URL}/auth/validate-password" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "password": "Senha@123"
  }'
```

### 5. Gerenciamento de Usuário
```bash
# Obter perfil do usuário
curl -X GET "${BASE_URL}/auth/me" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Atualizar perfil
curl -X PUT "${BASE_URL}/auth/me" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "name": "Novo Nome",
    "phoneNumber": "+5511999999999",
    "customAttributes": {
      "company": "Nova Empresa"
    }
  }'

# Deletar conta
curl -X DELETE "${BASE_URL}/auth/me" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Verificar status da conta
curl -X GET "${BASE_URL}/auth/me/status" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

### 6. MFA (Autenticação Multi-Fator)
```bash
# Habilitar MFA
curl -X POST "${BASE_URL}/auth/mfa/enable" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Desabilitar MFA
curl -X POST "${BASE_URL}/auth/mfa/disable" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "mfaCode": "123456"
  }'

# Verificar status MFA
curl -X GET "${BASE_URL}/auth/mfa/status" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Configurar método MFA
curl -X POST "${BASE_URL}/auth/mfa/setup" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "method": "SMS",
    "phoneNumber": "+5511999999999"
  }'
```

### 7. Grupos e Permissões
```bash
# Listar grupos do usuário
curl -X GET "${BASE_URL}/auth/groups" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Adicionar usuário a um grupo
curl -X POST "${BASE_URL}/auth/groups/admin/users" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "username": "usuario@exemplo.com"
  }'

# Remover usuário de um grupo
curl -X DELETE "${BASE_URL}/auth/groups/admin/users/usuario@exemplo.com" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"

# Listar permissões do usuário
curl -X GET "${BASE_URL}/auth/permissions" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}"
```

## Testes de Resiliência

### 1. Rate Limiting (Casos Detalhados)
```bash
# Teste de rate limiting por endpoint
for endpoint in "me" "validate-token" "groups"; do
  echo "Testando rate limiting para /auth/$endpoint"
  for i in {1..110}; do
    curl -s -X GET "${BASE_URL}/auth/$endpoint" \
      -H "Authorization: Bearer ${ACCESS_TOKEN}" \
      -w "\nStatus: %{http_code}\n"
    sleep 0.1
  done
done

# Teste de rate limiting com diferentes tokens
for i in {1..5}; do
  echo "Testando com token $i"
  TOKEN="token-$i"
  for j in {1..25}; do
    curl -s -X GET "${BASE_URL}/auth/me" \
      -H "Authorization: Bearer ${TOKEN}" \
      -w "\nStatus: %{http_code}\n"
    sleep 0.1
  done
done
```

### 2. Circuit Breaker (Casos Detalhados)
```bash
# Teste de circuit breaker com diferentes cenários
# 1. Falhas consecutivas
for i in {1..10}; do
  curl -s -X GET "${BASE_URL}/auth/me" \
    -H "Authorization: Bearer token-invalido" \
    -w "\nStatus: %{http_code}\n"
  sleep 0.1
done

# 2. Falhas intercaladas
for i in {1..20}; do
  if [ $((i % 2)) -eq 0 ]; then
    curl -s -X GET "${BASE_URL}/auth/me" \
      -H "Authorization: Bearer ${ACCESS_TOKEN}" \
      -w "\nStatus: %{http_code}\n"
  else
    curl -s -X GET "${BASE_URL}/auth/me" \
      -H "Authorization: Bearer token-invalido" \
      -w "\nStatus: %{http_code}\n"
  fi
  sleep 0.1
done

# 3. Teste de recuperação
echo "Aguardando recuperação do circuit breaker..."
sleep 2
curl -s -X GET "${BASE_URL}/auth/me" \
  -H "Authorization: Bearer ${ACCESS_TOKEN}" \
  -w "\nStatus: %{http_code}\n"
```

## Scripts Úteis

### 1. Script de Teste Completo
```bash
#!/bin/bash

# Configuração
BASE_URL="http://localhost:8080/api"
EMAIL="teste@exemplo.com"
PASSWORD="Senha@123"

# Função para testar endpoint
test_endpoint() {
  local method=$1
  local endpoint=$2
  local data=$3
  local token=$4
  
  echo "Testando $method $endpoint"
  if [ -n "$token" ]; then
    curl -s -X $method "${BASE_URL}$endpoint" \
      -H "${CONTENT_TYPE}" \
      -H "Authorization: Bearer $token" \
      -d "$data" \
      -w "\nStatus: %{http_code}\n"
  else
    curl -s -X $method "${BASE_URL}$endpoint" \
      -H "${CONTENT_TYPE}" \
      -d "$data" \
      -w "\nStatus: %{http_code}\n"
  fi
  echo "----------------------------------------"
}

# Teste completo
echo "Iniciando teste completo do auth-service"

# 1. Registro
echo "1. Testando registro"
test_endpoint "POST" "/auth/register" "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\",\"name\":\"Usuário Teste\"}"

# 2. Login
echo "2. Testando login"
RESPONSE=$(curl -s -X POST "${BASE_URL}/auth/login" \
  -H "${CONTENT_TYPE}" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
ACCESS_TOKEN=$(echo $RESPONSE | jq -r '.accessToken')

# 3. Validar token
echo "3. Testando validação de token"
test_endpoint "GET" "/auth/validate-token" "" "$ACCESS_TOKEN"

# 4. Obter perfil
echo "4. Testando obtenção de perfil"
test_endpoint "GET" "/auth/me" "" "$ACCESS_TOKEN"

# 5. Atualizar perfil
echo "5. Testando atualização de perfil"
test_endpoint "PUT" "/auth/me" "{\"name\":\"Novo Nome\"}" "$ACCESS_TOKEN"

# 6. Testar rate limiting
echo "6. Testando rate limiting"
for i in {1..110}; do
  test_endpoint "GET" "/auth/me" "" "$ACCESS_TOKEN"
  sleep 0.1
done

echo "Teste completo finalizado"
```

### 2. Script de Monitoramento
```bash
#!/bin/bash

# Configuração
BASE_URL="http://localhost:8080/api"
INTERVAL=5  # segundos

# Função para monitorar métricas
monitor_metrics() {
  while true; do
    echo "=== $(date) ==="
    
    # Health
    curl -s "${BASE_URL}/actuator/health" | jq '.'
    
    # Rate Limiter
    curl -s "${BASE_URL}/actuator/metrics/resilience4j.ratelimiter.available.permissions" | jq '.'
    
    # Circuit Breaker
    curl -s "${BASE_URL}/actuator/metrics/resilience4j.circuitbreaker.state" | jq '.'
    
    # Requisições HTTP
    curl -s "${BASE_URL}/actuator/metrics/http.server.requests" | jq '.'
    
    echo "----------------------------------------"
    sleep $INTERVAL
  done
}

# Iniciar monitoramento
monitor_metrics
```

## Notas de Segurança

1. **Ambiente de Produção**:
   - Sempre use HTTPS
   - Configure CORS adequadamente
   - Use secrets management
   - Implemente logging adequado
   - Monitore as métricas

2. **Tokens**:
   - Use tokens de curta duração
   - Implemente refresh token rotation
   - Revogue tokens comprometidos
   - Monitore uso anormal

3. **Senhas**:
   - Implemente política de senhas fortes
   - Use salt e hashing adequados
   - Implemente bloqueio após tentativas
   - Monitore tentativas de força bruta

4. **MFA**:
   - Recomende MFA para todos os usuários
   - Suporte múltiplos métodos
   - Implemente backup codes
   - Monitore tentativas de bypass

5. **Auditoria**:
   - Log de todas as operações
   - Monitoramento de atividades suspeitas
   - Alertas para comportamentos anormais
   - Retenção adequada de logs 