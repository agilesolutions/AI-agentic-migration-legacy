
output "service_name" {
  description = "MongoDB Kubernetes service name."
  value       = "${var.release_name}-mongodb"
}

output "port" {
  description = "MongoDB service port."
  value       = 27017
}

output "database" {
  description = "Application database name."
  value       = var.database
}

output "username" {
  description = "Application database username."
  value       = var.username
}

output "connection_host" {
  description = "Internal MongoDB DNS name."
  value       = "${var.release_name}-mongodb.${var.namespace}.svc.cluster.local"
}