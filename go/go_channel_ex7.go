package main

import (
	"fmt"
)

func producer(ch chan int) {

	for i := 0; i < 5; i++ {
		ch <- i
	}
}

func consume(ch chan int, done chan bool) {

	for i := 0; i < 5; i++ {
		v := <-ch
		fmt.Println("received: ", v)
	}

	done <- true
}

func main() {

	ch := make(chan int)
	done := make(chan bool)

	go producer(ch)
	go consume(ch, done)

	<-done
}
