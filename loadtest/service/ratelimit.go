package main

import (
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
	redisV9 "github.com/redis/go-redis/v9"
)

// var redis = redisV9.NewClient(&redisV9.Options{
// 	Addr:     "localhost:6379",
// 	Password: "SUPER_SECRET_PASSWORD",
// })

var clusterOptions = &redisV9.ClusterOptions{
	Addrs: []string{
		"redis-node-1:6379",
		"redis-node-2:6379",
		"redis-node-3:6379",
		"redis-node-4:6379",
		"redis-node-5:6379",
		"redis-node-6:6379",
	},
}
var redis = redisV9.NewClusterClient(clusterOptions)

func RateLimit(key string, rate int64, period time.Duration) gin.HandlerFunc {
	return func(c *gin.Context) {
		start := time.Now()

		if redis == nil {
			c.Next()
			return
		}
		periodInSec := int64(period.Seconds())
		windowStart := time.Now().Unix() / periodInSec
		rateLimitKey := fmt.Sprintf("ratelimit:%s:%d", key, windowStart)
		count, err := redis.Incr(c.Request.Context(), rateLimitKey).Result()
		if err != nil {
			c.Next()
			return
		}
		if count == 1 {
			go func() {
				_ = redis.Expire(c.Request.Context(), rateLimitKey, period).Err()
			}()
		}
		if count <= rate {
			duration := time.Since(start).Seconds()
			requestLatency.WithLabelValues(c.Request.URL.Path).Observe(duration)

			c.Next()
			return
		}
		windowEnd := (time.Now().Unix() / periodInSec * periodInSec) + periodInSec

		retryAfter := windowEnd - time.Now().Unix()
		remaining := rate - count
		if remaining < 0 {
			remaining = 0
		}
		c.Header("RateLimit-Limit", strconv.Itoa(int(rate)))
		c.Header("RateLimit-Remaining", strconv.Itoa(int(remaining)))
		c.Header("Retry-After", strconv.Itoa(int(retryAfter)))
		c.AbortWithError(http.StatusTooManyRequests, fmt.Errorf("rate limit"))

		duration := time.Since(start).Seconds()
		requestLatency.WithLabelValues(c.Request.URL.Path).Observe(duration)
	}
}
