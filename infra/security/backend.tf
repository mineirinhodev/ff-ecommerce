terraform {
  backend "s3" {
    bucket         = "deveminho-terraform"
    key            = "dev/security/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "terraform-lock"
  }
}
