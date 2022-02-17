package main

import (
	"fmt"
	"math"
)

func getSequence() func() int {

	i := 0
	return func() int {
		i += 1
		return i
	}
}

func mySqrt(x int) int {
	return int(math.Sqrt(float64(x)))
}

func myFunc(f func(x int) int) int {
	return f(99)
}

func main() {

	my_func := func(x float64) float64 {
		return math.Sqrt(x)
	}

	fmt.Println(my_func(100))

	nextNumber := getSequence()
	fmt.Println(nextNumber())
	fmt.Println(nextNumber())
	fmt.Println(nextNumber())

	fmt.Println(myFunc(mySqrt))
}
