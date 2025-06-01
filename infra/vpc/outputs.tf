output "vpc_id" {
  description = "ID da VPC"
  value       = aws_vpc.main.id
}

output "subnet_ids" {
  description = "IDs das subnets públicas"
  value = [aws_subnet.public[0].id, aws_subnet.public[1].id]
}