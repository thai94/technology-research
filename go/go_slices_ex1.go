package main

import "fmt"

func main() {

	number := []int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
	fmt.Printf("len = %d cap = %d slice = %v\n", len(number), cap(number), number)

	fmt.Println("numbers[1:4]: ", number[1:4])
	fmt.Println("number[:3]: ", number[:3])
	fmt.Println("number[4:]: ", number[4:])

}
