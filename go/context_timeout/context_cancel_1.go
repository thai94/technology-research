package main

import (
	"context"
	"fmt"
	"time"
)

type Result struct {
	err error
}

func func1(ctx context.Context, rs *Result, ch chan bool) {

	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	for {
		fmt.Println("hello")
		time.Sleep(100 * time.Microsecond)
		if ctx.Err() == context.DeadlineExceeded {
			fmt.Println("Canceled")
			ch <- true
			break
		}
	}
}

func main() {

	ctx := context.Background()
	rs := &Result{}
	ch := make(chan bool)
	go func1(ctx, rs, ch)
	<-ch

	// time.Sleep(5 * time.Minute)
}
