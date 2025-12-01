package main

import (
	"os"
	"path/filepath"
	"strings"
)

func check(e error) {
	if e != nil {
		panic(e)
	}
}

func getInput(file string) string {
	path := filepath.Join("../../inputs", file)
	data, err := os.ReadFile(path)
	check(err)
	return strings.Trim(string(data), " \n")
}

func myMod(num int, divisor int) int {
	if num%divisor < 0 {
		return num%divisor + divisor
	} else {
		return num % divisor
	}
}
