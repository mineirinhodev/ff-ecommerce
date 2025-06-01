variable "state_bucket_name" {
  description = "Nome do bucket S3 para armazenar o estado do Terraform"
  type        = string
  default     = "deveminho-terraform"
}

variable "dynamodb_table_name" {
  description = "Nome da tabela DynamoDB para lock do estado"
  type        = string
  default     = "terraform-lock"
} 