$ErrorActionPreference = "Stop"

$basePath = "C:\Users\emers\IdeaProjects\auth-service\infra"

$folders = @(
    "backend-bootstrap",
    "vpc",
    "security",
    "ecr",
    "ecs",
    "api-gateway",
    "api-gateway-domain"
)

foreach ($folder in $folders) {
    Write-Host "🚀 Deploying $folder..." -ForegroundColor Cyan
    Set-Location "$basePath\$folder"
    terraform init
    terraform apply -auto-approve
    Write-Host "✅ $folder deploy completed!" -ForegroundColor Green
}

Write-Host "🎉 Deploy completed for all modules!" -ForegroundColor Yellow
