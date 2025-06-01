variable "cluster_name" {
  description = "Nome do ECS Cluster"
  type        = string
  default     = "auth-cluster"
}

variable "service_name" {
  description = "Nome do ECS Service"
  type        = string
  default     = "auth-service"
}

variable "task_family" {
  description = "Família da Task"
  type        = string
  default     = "auth-task"
}

variable "cpu" {
  description = "CPU da task"
  type        = number
  default     = "512"
}

variable "memory" {
  description = "Memória da task"
  type        = number
  default     = "1024"
}

variable "container_port" {
  description = "Porta do container"
  type        = number
  default     = 8080
}
