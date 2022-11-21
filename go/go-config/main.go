package main

import (
	"fmt"

	"github.com/spf13/viper"
)

func main() {

	folder := "./config"
	viper.AddConfigPath(folder)
	viper.SetConfigName("qc")
	viper.SetConfigType("yaml")

	err := viper.ReadInConfig()
	if err != nil {
		fmt.Println("ERROR")
	}

	port := viper.GetInt64("server.port")
	fmt.Println(port)
}
