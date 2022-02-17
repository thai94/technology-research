package main

import (
	"fmt"
	"time"
)

func f1(ch chan int) {
	fmt.Println("F1 -> 1")
	ch <- 5
	fmt.Println("F1 -> 2")
	ch <- 10
}

func f2(ch chan int) {
	fmt.Println("F2 -> 1")
	a := <-ch
	fmt.Println("F2 -> 1")
	b := <-ch
	fmt.Println(a + b)
}

func main() {

	ch := make(chan int, 10)
	go f1(ch)
	go f2(ch)

	time.Sleep(10 * time.Second)
}
