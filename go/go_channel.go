package main

import (
	"fmt"
	"time"
)

func hello(done chan bool) {
	fmt.Println("Hello world goroutine")
	time.Sleep(4 * time.Second)
	fmt.Println("hello go routine awake and going to write to done")
	done <- true
}

func main() {

	done := make(chan bool)
	go hello(done)
	<-done
	fmt.Println("main function")
}
