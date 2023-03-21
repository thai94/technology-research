package main

import "fmt"

func main() {

	var countryCapitalMap map[string]string

	countryCapitalMap = make(map[string]string)
	countryCapitalMap["France"] = "Paris"
	countryCapitalMap["Italy"] = "Rome"
	countryCapitalMap["Japan"] = "Tokyo"
	countryCapitalMap["India"] = "New Delhi"

	capital, ok := countryCapitalMap["United States"]
	if ok {
		fmt.Println("Calital of United States is", capital)
	} else {
		fmt.Println("Capital of United States is not present")
	}

	delete(countryCapitalMap, "France")

	capital1, ok1 := countryCapitalMap["France"]
	if ok1 {
		fmt.Println("Calital of France is", capital1)
	} else {
		fmt.Println("Capital of France is not present")
	}
}
