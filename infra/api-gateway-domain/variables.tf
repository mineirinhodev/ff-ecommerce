variable "domain_name" {
  description = "Nome completo do domínio (ex: api.deveminho.com.br)"
  type        = string
}

variable "certificate_arn" {
  description = "ARN do certificado SSL no ACM"
  type        = string
}

variable "hosted_zone_id" {
  description = "ID da zona hospedada no Route 53"
  type        = string
}

variable "subdomain" {
  description = "Subdomínio para o API Gateway (ex: api)"
  type        = string
}
