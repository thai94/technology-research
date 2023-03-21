package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {

	name := "ABC"
	age := 25
	now := time.Now()

	fmt.Printf("Name: %s \n", name)
	fmt.Printf("Age: %d \n", age)
	fmt.Printf("Now: %s \n", now.Format("2006-01-02 15:04"))

	i := 1
	if i == 1 {
		fmt.Println("IF i = 1")
	}

	for i := 1; i <= 10; i++ {
		fmt.Printf("i = %d\n", i)
	}

	print_name("ABC")

	prices := []int{
		5, 10, 15, 20, 25,
	}

	for _, p := range prices {
		fmt.Println(p)
	}

	m := map[string]int{
		"Mon":  1,
		"Tus":  2,
		"Wed":  3,
		"Thus": 4,
		"Fri":  5,
	}

	for k, v := range m {
		fmt.Printf("k: %s, v: %d \n", k, v)
	}

	student := &Student{
		FirstName: "Nguyen",
		LastName:  "Thai",
		DoB:       "2000/01/01",
	}

	fmt.Printf("A Student: %s, %s, %s \n", student.FirstName, student.LastName, student.DoB)

	student.Print()

	wg := &sync.WaitGroup{}
	mx := &sync.Mutex{}
	dead_num := 0
	alive_num := 0
	for i := 1; i <= 100000; i++ {
		wg.Add(1)
		// go print_hello(i, wg, dead_num, alive_num)

		go func(node_ith int) {
			// fmt.Printf("Hello: %d \n", node_ith)
			mx.Lock()
			if node_ith%2 == 0 {
				dead_num++
			} else {
				alive_num++
			}
			mx.Unlock()
			wg.Done()
		}(i)
	}

	wg.Wait()
	fmt.Printf("dead_num: %d \n", dead_num)
	fmt.Printf("dead_num: %d \n", alive_num)

	// time.Sleep(5 * time.Second)
	// 2 dead
	// => 3 dead he thong co van de
}

func print_hello(node_ith int, wg *sync.WaitGroup, dead_num, alive_num int) {

	fmt.Printf("Hello: %d \n", node_ith)
	if node_ith%2 == 0 {
		dead_num++
	} else {
		alive_num++
	}
	wg.Done()
}

type Student struct {
	FirstName string
	LastName  string
	DoB       string
}

func (student *Student) Print() {
	fmt.Printf("A Student: %s, %s, %s \n", student.FirstName, student.LastName, student.DoB)
}

func print_name(name string) {
	fmt.Printf("Hello: %s \n", name)
}
