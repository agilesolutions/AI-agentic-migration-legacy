# Deploy kafka Natively via the Terminal

```
# 1. Ensure you are looking at Docker Desktop's Kubernetes engine
kubectl config use-context docker-desktop

# 2. Add and index the Redpanda Helm repository
helm repo add redpanda https://charts.redpanda.com
helm repo update

# 3. Create a isolated namespace 
kubectl create namespace kafka-dev

# 4. Install the chart using your custom yaml configuration overrides
helm install local-kafka redpanda/redpanda --namespace kafka-dev -f redpanda-local.yaml
helm upgrade local-kafka redpanda/redpanda -n kafka-dev --reuse-values -f redpanda-local.yaml


# 5. Check the status of the deployment
kubectl -n kafka-dev rollout status statefulset kafka -w
kubectl get pods -n kafka-dev -w

# 6. Upgrade the deployment with any changes to your configuration
helm upgrade local-kafka redpanda/redpanda --namespace kafka-dev -f redpanda-local.yaml
kubectl delete pods -n kafka-dev --all

# 7. Access the Redpanda Console
kubectl get service local-kafka-console -n kafka-dev
kubectl port-forward deployment/local-kafka-console 8080:8080 -n kafka-dev
http://localhost:8080
```

## Create first topic

```
kubectl exec -it statefulset/kafka -n kafka-dev -- rpk topic create my-first-topic
```