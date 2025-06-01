output "domain_name" {
  description = "O nome do domínio criado no API Gateway"
  value       = aws_apigatewayv2_domain_name.api.domain_name
}

output "api_gateway_domain_target" {
  description = "Endpoint DNS do API Gateway para usar no Route53"
  value       = aws_apigatewayv2_domain_name.api.domain_name_configuration[0].target_domain_name
}
