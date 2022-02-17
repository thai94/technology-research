package main

import (
	"errors"
	"fmt"
	"math"
)

func Sqrt(value float64) (float64, error) {
	if value < 0 {
		return 0, errors.New("Math: negative number pased to Sqrt")
	}

	return math.Sqrt(value), nil
}

func main() {

	result, err := Sqrt(-100)
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println(result)
	}
}
