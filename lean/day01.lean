open IO.FS (Handle)

def splitLines (s : String) := s.splitOn "\n"

def datafy (s : String) :=
  let direction := s.take 1
  let distance := String.toNat! (s.drop 1)
  -- I tried with a structure first, but deriving Repr was not good enough
  -- for a print to see what I was actually doing
  (direction, distance)

-- there was a mean bug here when I declared dial as Nat
-- turning too far to the left would just result in 0 and not "underflow"
def safeTurn (dial : Int) (instr : String × Nat) :=
  (if instr.fst == "L"
     then dial - instr.snd
     else dial + instr.snd) % 100

def reductionsAux (acc : Array a) (f : a -> b -> a) (v : a) (l : List b) :=
  match l with
  | [] => acc
  | x :: xs =>
    reductionsAux (acc.push (f v x)) f (f v x) xs

-- debugging function used to find the bug above
def reductions (f : a -> b -> a) (v : a) (l : List b) :=
  reductionsAux #[v] f v l

def countZeroes (numZeroes : Nat) (dial : Int) (l : List (String × Nat)) :=
  match l with
  | [] => numZeroes
  | x :: xs =>
    let newDial := safeTurn dial x
    if newDial == 0 then
      countZeroes (numZeroes + 1) newDial xs
      else countZeroes numZeroes newDial xs

def newZeroes (dial : Int) (direction : String) (distance : Nat) :=
  let fullRotations := distance / 100
  let restDistance := distance % 100
  -- this seems a little more elegant than my Clojure solution
  if dial == 0 then fullRotations
  else if direction == "L" && restDistance >= dial then fullRotations + 1
  else if direction == "R" && restDistance >= 100 - dial then fullRotations + 1
  else fullRotations

def countZeroes2 (numZeroes : Nat) (dial : Int) (l : List (String × Nat)) :=
  match l with
  | [] => numZeroes
  | (x,y) :: xs =>
    let newDial := safeTurn dial (x,y)
    countZeroes2 (numZeroes + (newZeroes dial x y)) newDial xs

def main : IO Unit := do
  let handle <- Handle.mk "../inputs/input01" .read
  let raw <- handle.readToEnd
  let split :=  (splitLines raw).dropLast
  let data := split.map datafy

  --let safestate := data.foldl safeTurn 50
  --let reds := reductions safeTurn 50 data

  let solution_a := countZeroes 0 50 data
  let solution_b := countZeroes2 0 50 data

  -- Yeah, I could get used to it this year. Maybe.
  IO.println (solution_a, solution_b)


#eval main
