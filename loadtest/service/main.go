package main

import (
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

func helloWorld(c *gin.Context) {
	c.JSON(http.StatusOK, "ok")
}

func main() {
	router := gin.Default()
	router.GET("/helloworld", RateLimit("helloworld", 100, time.Second), helloWorld)
	// router.GET("/helloworld", helloWorld)

	router.Run("localhost:8080")
}
