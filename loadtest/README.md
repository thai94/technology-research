docker exec -it  be-api-redis-node-1-1 bash


redis-cli --cluster create --cluster-replicas 1 \
  redis-node-1:6379 \
  redis-node-2:6379 \
  redis-node-3:6379 \
  redis-node-4:6379 \
  redis-node-5:6379 \
  redis-node-6:6379

k6 run --summary-trend-stats="avg,min,med,p(95),p(99)" .\script.js