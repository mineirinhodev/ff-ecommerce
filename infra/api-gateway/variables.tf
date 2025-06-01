variable "domain_name" {
  description = "FQDN do domínio"
  type        = string
}

variable "subdomain" {
  description = "Subdomínio (ex: api)"
  type        = string
}

variable "certificate_arn" {
  description = "ARN do certificado SSL no ACM"
  type        = string
}

variable "zone_id" {
  description = "ID da zona hospedada no Route53"
  type        = string
}
