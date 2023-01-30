package main

import (
	"fmt"
	"time"
)

func main() {

	responses := make(chan string, 1)
	go func() {
		responses <- request("abc.com")
	}()

	go func() {
		responses <- request("abc2.com")
	}()

	go func() {
		responses <- request("abc1.com")
	}()

	time.Sleep(5 * time.Second)

	fmt.Println(len(responses))
	fmt.Println(<-responses)

}

func request(hostname string) string {
	return hostname
}
