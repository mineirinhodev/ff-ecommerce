
resource "aws_ecr_repository" "auth_service" {
  name         = "auth-service"
  force_delete = true
}

terraform {
  backend "s3" {
    bucket         = "deveminho-terraform"
    key            = "dev/ecr/terraform.tfstate"
  region         = "us-east-1"
  encrypt        = true
  dynamodb_table = "terraform-lock"
}
}
