
# Importa VPC existente via Remote State
data "terraform_remote_state" "vpc" {
  backend = "s3"
  config = {
    bucket = "deveminho-terraform"
    key    = "dev/vpc/terraform.tfstate"
    region = "us-east-1"
  }
}

# Security Group para o serviço
resource "aws_security_group" "auth_sg" {
  name        = "auth-sg"
  description = "Allow HTTP traffic"
  vpc_id      = data.terraform_remote_state.vpc.outputs.vpc_id

  ingress {
    description = "Allow HTTP from anywhere"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "auth-sg"
  }
}


