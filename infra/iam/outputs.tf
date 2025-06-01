output "execution_role_arn" {
  description = "ARN do role de execução do ECS"
  value       = aws_iam_role.ecs_task_execution_role.arn
}