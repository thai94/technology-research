package main

import (
	"fmt"
	"time"
)

func write(ch chan int) {

	for i := 0; i < 5; i++ {
		ch <- i
		fmt.Println("Successfully wrote", i, "to ch")
	}

	close(ch)
}

func main() {
	ch := make(chan int)
	go write(ch)
	time.Sleep(2 * time.Second)
	for v := range ch {
		fmt.Println("read value", v, "from ch")
		time.Sleep(2 * time.Second)
	}
}
