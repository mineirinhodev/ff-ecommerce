output "zone_id" {
  description = "Hosted Zone ID"
  value       = aws_route53_zone.this.zone_id
}

output "certificate_arn" {
  description = "ACM Certificate ARN"
  value       = aws_acm_certificate_validation.cert_validation.certificate_arn
}

output "domain_name" {
  value = var.domain_name
}
