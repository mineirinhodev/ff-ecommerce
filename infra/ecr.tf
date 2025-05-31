
resource "aws_ecr_repository" "auth_service" {
  name         = "auth-service"
  force_delete = true
}