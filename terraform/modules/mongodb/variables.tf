
variable "namespace" {
  description = "Kubernetes namespace for MongoDB."
  type        = string
  default     = "database"
}

variable "release_name" {
  description = "Helm release name."
  type        = string
  default     = "mongodb"
}

variable "chart_version" {
  description = "Pinned Bitnami MongoDB Helm chart version."
  type        = string
  default     = "19.3.1"
}

variable "database" {
  description = "Application database name."
  type        = string
  default     = "notebook_intelligence"
}

variable "username" {
  description = "Application database username."
  type        = string
  default     = "intelligence"
}

variable "root_password" {
  description = "MongoDB root password. Supply through a secret variable."
  type        = string
  sensitive   = true
}

variable "password" {
  description = "MongoDB application user's password."
  type        = string
  sensitive   = true
}

variable "storage_size" {
  description = "Persistent volume size."
  type        = string
  default     = "2Gi"
}

variable "storage_class" {
  description = "Storage class. Empty uses the cluster default."
  type        = string
  default     = ""
}

variable "cpu_request" {
  type    = string
  default = "100m"
}

variable "memory_request" {
  type    = string
  default = "256Mi"
}

variable "cpu_limit" {
  type    = string
  default = "500m"
}

variable "memory_limit" {
  type    = string
  default = "768Mi"
}