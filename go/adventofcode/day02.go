package main

import (
	"fmt"
	"strconv"
	"strings"
)

func rangeTuplise(ranger string) []int {
	l := strings.Split(ranger, "-")
	var res []int
	for _, e := range l {
		s, err := strconv.Atoi(e)
		check(err)
		res = append(res, s)
	}
	return res
}

func checkNumForRepeat2(n int) bool {
	ii := strconv.Itoa(n)
	length := len(ii)
	if length%2 == 0 {
		l := length / 2
		if ii[:l] == ii[l:] {
			return true
		} else {
			return false
		}
	}
	return false
}

func checkNumForRepeatAny(n int) bool {
	ii := strconv.Itoa(n)
	length := len(ii)

	for i := 1; i <= length/2; i++ {
		if length%i == 0 {
			ref := ii[:i]
			same := true
			for offset := i; offset < length; offset = offset + i {
				if ii[offset:offset+i] != ref {
					same = false
					break
				}
			}
			if same {
				return true
			}

		}
	}
	return false
}

func filterIntervalForRepeat2(tuple []int) []int {
	var invalid []int
	for i := tuple[0]; i <= tuple[1]; i++ {
		if checkNumForRepeat2(i) {
			invalid = append(invalid, i)
		}
	}
	return invalid
}

func filterIntervalForRepeatAny(tuple []int) []int {
	var invalid []int
	for i := tuple[0]; i <= tuple[1]; i++ {
		if checkNumForRepeatAny(i) {
			invalid = append(invalid, i)
		}
	}
	return invalid
}

func sumList(list []int) int {
	res := 0
	for _, e := range list {
		res = res + e
	}
	return res
}

func main() {
	input := getInput("input02")

	l := strings.Split(strings.Trim(input, " \n"), ",")
	var ll [][]int
	for _, e := range l {
		ll = append(ll, rangeTuplise(e))
	}

	var offenders []int
	for _, ranger := range ll {
		offenders = append(offenders, filterIntervalForRepeat2(ranger)...)
	}

	var offenders2 []int
	for _, ranger := range ll {
		offenders2 = append(offenders2, filterIntervalForRepeatAny(ranger)...)
	}

	fmt.Println(sumList(offenders))
	fmt.Println(sumList(offenders2))

}
