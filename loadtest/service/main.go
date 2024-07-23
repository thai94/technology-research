package main

import (
	"errors"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func helloWorld(c *gin.Context) {
	c.JSON(http.StatusOK, "ok")
}

var (
	requestLatency = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "ratelimit_request_duration_seconds",
			Help:    "Duration of HTTP requests in seconds",
			Buckets: prometheus.DefBuckets, // Default buckets
		},
		[]string{"path"},
	)
)

func init() {
	prometheus.Register(requestLatency)
}

func main() {
	router := gin.Default()
	router.GET("/helloworld", RateLimit("helloworld", 100, time.Second), helloWorld)
	// router.GET("/helloworld", helloWorld)
	router.GET("/metrics", gin.WrapH(promhttp.Handler()))

	svc := &http.Server{
		Addr:              ":8080",
		Handler:           router,
		ReadHeaderTimeout: 60 * time.Second,
	}

	if err := svc.ListenAndServe(); !errors.Is(err, http.ErrServerClosed) {
		fmt.Printf("http server: ListenAndServe failed: %v", err)
		return
	}

	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, syscall.SIGTERM, os.Interrupt)
	<-shutdown
}
