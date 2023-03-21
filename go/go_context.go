package main

import (
	"context"
	"https://gitlab.zalopay.vn/zp-platform/tracing"
)

func func1(pContext context.Context) {

	ctx := context.WithValue(pContext, "key", "test")

}

func main() {

	ctx, cancel := context.Background()

}
