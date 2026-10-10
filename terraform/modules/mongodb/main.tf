resource "helm_release" "mongodb" {
  name       = var.release_name
  repository = "oci://registry-1.docker.io/bitnamicharts"
  chart      = "mongodb"
  version    = var.chart_version
  namespace  = var.namespace

  atomic          = true
  cleanup_on_fail = true
  timeout         = 600
  wait            = true

  values = [
    yamlencode({
      architecture = "standalone"

      auth = {
        enabled      = true
        rootPassword = var.root_password
        usernames    = [var.username]
        passwords    = [var.password]
        databases    = [var.database]
      }

      persistence = {
        enabled      = true
        size         = var.storage_size
        storageClass = var.storage_class
      }

      service = {
        type = "ClusterIP"
        ports = {
          mongodb = 27017
        }
      }

      resources = {
        requests = {
          cpu    = var.cpu_request
          memory = var.memory_request
        }
        limits = {
          cpu    = var.cpu_limit
          memory = var.memory_limit
        }
      }

      metrics = {
        enabled = false
      }
    })
  ]

}