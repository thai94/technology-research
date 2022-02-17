package main

import "fmt"

func main() {

	var balance = [5]float32{1000.0, 2.0, 3.4, 7.0, 50.0}

	for i := 0; i < 5; i++ {
		fmt.Println(balance[i])
	}
}
