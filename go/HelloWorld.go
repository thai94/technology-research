package main

import "fmt"

func main() {

	fmt.Println("Hello, World!")

	x := true

	var y float32 = 3.14e-10

	var age int = 15

	fmt.Println(x)
	fmt.Println(y)
	fmt.Println(age)

	var name string = "hello, dear"
	fmt.Println(name)

	const LENGTH int = 10
	const WIDTH int = 5

	fmt.Println("value of area: %s", LENGTH*WIDTH)

	var a int = 10
	var b int
	b = 100

	if a < 20 {
		println("Hello")
	}

	fmt.Println(b)

	var sum int = 0
	for i := 0; i < 100; i++ {
		sum += i
	}
	fmt.Println(mySum(100))
	fmt.Println(swap("10", "9"))
}

func mySum(n int) int {

	var sum int = 0
	for i := 0; i < n; i++ {
		sum += i
	}

	return sum
}

func swap(x, y string) (string, string) {

	return y, x
}
