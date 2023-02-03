package main

func isPalindrome(x int) bool {

	if x < 0 {
		return false
	}

	origin_x := x
	startMul := false

	reverse_num := 0
	for {
		n := x % 10
		if n > 0 {
			startMul = true
		}
		if startMul {
			reverse_num = reverse_num*10 + n
		}
		println(reverse_num)
		x = x / 10
		if x == 0 {
			break
		}
	}

	return origin_x == reverse_num
}

func main() {
	println(isPalindrome(100))
}
