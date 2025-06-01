# Verifica se o Terraform está instalado
if (-not (Get-Command terraform -ErrorAction SilentlyContinue)) {
    Write-Error "Terraform não está instalado. Por favor, instale o Terraform primeiro."
    exit 1
}

# Lista de diretórios a serem destruídos na ordem reversa da criação
$dirs = @(
    "api-gateway-domain",
    "api-gateway",
    "ecs",
    "ecr",
    "security",
    "vpc",
    "backend-bootstrap"
)

foreach ($dir in $dirs) {
    Write-Host "Destruindo recursos em $dir..." -ForegroundColor Yellow
    try {
        Set-Location ".\$dir"

        terraform init
        terraform destroy -auto-approve

        Set-Location ".."
    } catch {
        Write-Error "Erro ao destruir recursos em $dir. Verifique manualmente."
        exit 1
    }
}

Write-Host "✅ Todos os recursos foram destruídos com sucesso!" -ForegroundColor Green
