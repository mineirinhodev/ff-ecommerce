# Tópico SNS para alertas
resource "aws_sns_topic" "ecs_alerts" {
  name = "ecs-alerts-${var.service_name}"
}

# Inscrição de email no tópico SNS
resource "aws_sns_topic_subscription" "email" {
  topic_arn = aws_sns_topic.ecs_alerts.arn
  protocol  = "email"
  endpoint  = var.alert_email
}

# Alarme de CPU Baixa
resource "aws_cloudwatch_metric_alarm" "cpu_low" {
  alarm_name          = "${var.service_name}-cpu-utilization-low"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "2"
  metric_name        = "CPUUtilization"
  namespace          = "AWS/ECS"
  period             = "300"
  statistic          = "Average"
  threshold          = "10"
  alarm_description  = "Média de uso de CPU abaixo de 10% por 10 minutos - possível problema no serviço"
  alarm_actions      = [aws_sns_topic.ecs_alerts.arn]

  dimensions = {
    ClusterName = aws_ecs_cluster.this.name
    ServiceName = aws_ecs_service.this.name
  }
}

# Alarme de Memória Baixa
resource "aws_cloudwatch_metric_alarm" "memory_low" {
  alarm_name          = "${var.service_name}-memory-utilization-low"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "2"
  metric_name        = "MemoryUtilization"
  namespace          = "AWS/ECS"
  period             = "300"
  statistic          = "Average"
  threshold          = "5"
  alarm_description  = "Média de uso de memória abaixo de 5% por 10 minutos - possível problema no serviço"
  alarm_actions      = [aws_sns_topic.ecs_alerts.arn]

  dimensions = {
    ClusterName = aws_ecs_cluster.this.name
    ServiceName = aws_ecs_service.this.name
  }
}

# Alarme de Tarefas em Execução
resource "aws_cloudwatch_metric_alarm" "running_tasks" {
  alarm_name          = "${var.service_name}-running-tasks"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "1"
  metric_name        = "RunningTaskCount"
  namespace          = "AWS/ECS"
  period             = "300"
  statistic          = "Average"
  threshold          = "1"
  alarm_description  = "Nenhuma tarefa em execução no serviço"
  alarm_actions      = [aws_sns_topic.ecs_alerts.arn]

  dimensions = {
    ClusterName = aws_ecs_cluster.this.name
    ServiceName = aws_ecs_service.this.name
  }
} 