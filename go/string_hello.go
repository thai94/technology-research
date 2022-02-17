package main

import (
	"fmt"
	"strings"
)

func main() {

	var greeting = "Hello world!"
	fmt.Println("String Length: ", len(greeting))

	greetings := []string{"hello", "world"}
	fmt.Println(strings.Join(greetings, " "))
}
