# 🔗 Importa a VPC
data "terraform_remote_state" "vpc" {
  backend = "s3"
  config = {
    bucket = "deveminho-terraform"
    key    = "dev/vpc/terraform.tfstate"
    region = "us-east-1"
  }
}

# 🔗 Importa Security Group
data "terraform_remote_state" "security" {
  backend = "s3"
  config = {
    bucket = "deveminho-terraform"
    key    = "dev/security/terraform.tfstate"
    region = "us-east-1"
  }
}

# 🔗 Importa ECR
data "terraform_remote_state" "ecr" {
  backend = "s3"
  config = {
    bucket = "deveminho-terraform"
    key    = "dev/ecr/terraform.tfstate"
    region = "us-east-1"
  }
}

# 🔗 Importa IAM
data "terraform_remote_state" "iam" {
  backend = "s3"
  config = {
    bucket = "deveminho-terraform"
    key    = "dev/iam/terraform.tfstate"
    region = "us-east-1"
  }
}

# 🔥 CloudWatch Log Group
resource "aws_cloudwatch_log_group" "this" {
  name              = "/ecs/${var.service_name}"
  retention_in_days = 7
}

# 🔥 ECS Cluster
resource "aws_ecs_cluster" "this" {
  name = var.cluster_name
}

# 🔥 Task Definition
resource "aws_ecs_task_definition" "this" {
  family                   = var.task_family
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.cpu
  memory                   = var.memory
  execution_role_arn       = data.terraform_remote_state.iam.outputs.execution_role_arn

  container_definitions = jsonencode([{
    name      = var.service_name
    image     = "${data.terraform_remote_state.ecr.outputs.repository_url}:latest"
    cpu       = var.cpu
    memory    = var.memory
    essential = true

    portMappings = [{
      containerPort = var.container_port
      hostPort      = var.container_port
      protocol      = "tcp"
    }]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = "/ecs/${var.service_name}"
        awslogs-region        = "us-east-1"
        awslogs-stream-prefix = "ecs"
      }
    }
  }])
}

# 🔥 ECS Service
resource "aws_ecs_service" "this" {
  name            = var.service_name
  cluster         = aws_ecs_cluster.this.id
  task_definition = aws_ecs_task_definition.this.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets          = data.terraform_remote_state.vpc.outputs.subnet_ids
    security_groups  = [data.terraform_remote_state.security.outputs.security_group_id]
    assign_public_ip = true
  }

  lifecycle {
    ignore_changes = [task_definition]
  }
}
