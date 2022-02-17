package main

import "fmt"

type Books struct {
	title   string
	author  string
	subject string
	book_id int
}

func main() {

	var book1 Books

	book1.title = "Go programming"
	book1.author = "ThaiNQ"
	book1.subject = "GO programming tutorial"
	book1.book_id = 1

	fmt.Printf("Book title:  %s", book1.title)
}
