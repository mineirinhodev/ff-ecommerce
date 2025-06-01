output "state_bucket_name" {
  value       = aws_s3_bucket.terraform_state.id
  description = "Nome do bucket S3 criado para o estado do Terraform"
}

output "dynamodb_table_name" {
  value       = aws_dynamodb_table.terraform_locks.name
  description = "Nome da tabela DynamoDB criada para lock do estado"
} 