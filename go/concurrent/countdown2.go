package main

import (
	"fmt"
	"os"
	"time"
)

func main() {

	abort := make(chan struct{})
	go func() {
		os.Stdin.Read(make([]byte, 1))
		abort <- struct{}{}
	}()

	fmt.Println("Commencing countdown. Press return to abort.")
	select {
	case <-time.After(10 * time.Second):
		// do nothing
	case <-abort:
		fmt.Println("Launch aborted!")
		return
	}

	fmt.Println("launch")
}
