package main

import "fmt"

func main() {

	var a int = 10
	fmt.Printf("Address of variable: %x \n", &a)

	var ip *int
	ip = &a
	fmt.Printf("Address stored in ip variable: %x\n", ip)
	fmt.Printf("Value of *ip variable: %d\n", *ip)

	var ptr *int
	if ptr == nil {
		fmt.Printf("The value of ptr is: %x\n", ptr)
	}

}
