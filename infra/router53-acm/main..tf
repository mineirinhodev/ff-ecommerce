################
# Route 53 Zone
################

resource "aws_route53_zone" "this" {
  name = var.domain_name
}

##################
# ACM Certificate
##################

resource "aws_acm_certificate" "cert" {
  domain_name               = "*.${var.domain_name}"
  validation_method         = "DNS"
  subject_alternative_names = ["${var.domain_name}"]

  lifecycle {
    create_before_destroy = true
  }
}

##############################
# ACM Certificate Validation
##############################

resource "aws_route53_record" "validation" {
  for_each = {
    for dvo in aws_acm_certificate.cert.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      type   = dvo.resource_record_type
      record = dvo.resource_record_value
    }
  }

  name    = each.value.name
  type    = each.value.type
  zone_id = aws_route53_zone.this.zone_id
  ttl     = 60
  records = [each.value.record]
}

resource "aws_acm_certificate_validation" "cert_validation" {
  certificate_arn         = aws_acm_certificate.cert.arn
  validation_record_fqdns = [for record in aws_route53_record.validation : record.fqdn]
}
