docker exec -it service-redis-node-1-1 bash


redis-cli --cluster create --cluster-replicas 1 \
  redis-node-1:6379 \
  redis-node-2:6379 \
  redis-node-3:6379 \
  redis-node-4:6379 \
  redis-node-5:6379 \
  redis-node-6:6379
