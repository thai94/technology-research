package main

import "fmt"

func main() {

	var numbers []int
	if numbers == nil {
		fmt.Println("Empty numbers")
	}

	numbers = make([]int, 3, 5)

	if numbers == nil {
		fmt.Println("Empty numbers")
	}

	fmt.Printf("len=%d cap=%d slice:%v\n", len(numbers), cap(numbers), numbers)

	for i := 0; i < len(numbers); i++ {
		numbers[i] = i + 1
	}

	for i := 0; i < len(numbers); i++ {
		fmt.Println(numbers[i])
	}

}
