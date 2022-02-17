package main

import "fmt"

func swap(x *int, y *int) {

	var temp int
	temp = *x
	*x = *y
	*y = temp
}

func main() {

	var a int = 100
	var b int = 200

	fmt.Println("Before swap, value of a: %d", a)
	fmt.Println("Before swap, value of b: %d", b)
	swap(&a, &b)

	fmt.Println("After swap, value of a: %d", a)
	fmt.Println("After swap, value of b: %d", b)
}
