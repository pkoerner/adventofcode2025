package main

import (
	"fmt"
	"strconv"
	"strings"
)

type Direction int

const (
	left Direction = iota
	right
)

type Instruction struct {
	direction Direction
	distance  int
}

func datafyInstruction(instr string) Instruction {
	var d Direction
	if instr[0:1] == "L" {
		d = left
	} else {
		d = right
	}
	distance, err := strconv.Atoi(instr[1:])
	check(err)
	res := Instruction{d, distance}
	return res
}

func dialSafe(dial int, instr Instruction) int {
	var newdial int
	if instr.direction == left {
		newdial = dial - instr.distance
	} else {
		newdial = dial + instr.distance
	}
	newdial = myMod(newdial, 100)
	return newdial
}

func dialSafeFullA(l []Instruction) int {
	dial := 50
	countZero := 0

	for _, e := range l {
		dial = dialSafe(dial, e)
		if dial == 0 {
			countZero++
		}
	}

	return countZero
}

func dialSafeFullB(l []Instruction) int {
	dial := 50
	countZero := 0

	for _, e := range l {
		oldDial := dial
		dial = dialSafe(dial, e)
		rotations := e.distance / 100
		restDistance := myMod(e.distance, 100)
		if oldDial != 0 && e.direction == left && restDistance >= oldDial {
			countZero = countZero + rotations + 1
		} else if oldDial != 0 && e.direction == right && restDistance >= (100-oldDial) {
			countZero = countZero + rotations + 1
		} else {
			countZero = countZero + rotations
		}
	}

	return countZero
}

func mainDay01() {
	input := getInput("input01")
	l := strings.Split(input, "\n")
	var list2 []Instruction
	for _, e := range l {
		list2 = append(list2, datafyInstruction(e))
	}

	fmt.Println(dialSafeFullA(list2))
	fmt.Println(dialSafeFullB(list2))

}
