package main

import (
	"fmt"

	"github.com/Shopify/sarama"
)

func main() {

	producer, err := sarama.NewSyncProducer([]string{"10.60.45.2:9092", "10.60.45.5:9092", "10.60.45.6:9092"}, nil)
	if err != nil {
		fmt.Println(err)
	}

	defer func() {
		if err := producer.Close(); err != nil {
			fmt.Println(err)
		}
	}()

	msg := &sarama.ProducerMessage{Topic: "my_topic", Value: sarama.StringEncoder("testing 123")}
	partition, offset, err := producer.SendMessage(msg)

	if err != nil {
		fmt.Printf("FAILED to send message: %s\n", err)
	} else {
		fmt.Printf("message sent to partition %d at offset %d\n", partition, offset)
	}
}
