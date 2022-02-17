package main

import (
	"context"
	"log"
	"time"

	pb "github.com/thai94/technology-research/tree/master/go/grpc/helloworld"
	"google.golang.org/grpc"
)

const (
	address     = "localhost:50051"
	defaultName = "world"
)

func main() {

	conn, err := grpc.Dial(address, grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {

		log.Fatal("did not connect: %v", err)
	}

	defer conn.Close()
	c := pb.NewGreeterClient(conn)

	name := defaultName

	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()

	r, err := c.SayHello(ctx, &pb.HelloRequest{Name: name})
	if err != nil {
		log.Fatal("could not greet: %v", err)
	}
	log.Printf("Greeting: %s", r.GetMessage())

}
