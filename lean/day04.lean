import Std.Data.HashMap
open IO.FS (Handle)



def splitLines (s : String) := s.splitOn "\n"

def datafy (s : String) :=
  let direction := s.take 1
  let distance := String.toNat! (s.drop 1)
  -- I tried with a structure first, but deriving Repr was not good enough
  -- for a print to see what I was actually doing
  (direction, distance)

--def fillMap (s : String) (x : Nat) (y : Nat) (m : Std.HashMap (Nat × Nat) Char) :=
--  if s.isEmpty then
--    m
--  else
--    let c := s.front
--    let m' := m.insert (x,y) c
--    if c == "\n".front then
      -- keep original map, do not insert newline
--      fillMap s[1:] 0 (y + 1) m
--    else
--      fillMap s[1:] (x + 1) y m'
--    termination_by structural s

def insertCharIdx (tripel : (Std.HashMap (Int × Int) Char × Int × Int)) (c : Char):=
  let m := tripel.fst
  let x := tripel.snd.fst
  let y := tripel.snd.snd
  if c == "\n".front then
    (m,0,y+1)
  else
    if c == '@' then
      (m.insert (x,y) c,x+1,y)
    else (m, x+1, y)

def fillMap2 (s : String) (m : Std.HashMap (Int × Int) Char) :=
  s.foldl insertCharIdx (m, 0, 0)


def griddify (s : String) :=
  let m : Std.HashMap (Int × Int) Char := (Std.HashMap.emptyWithCapacity)
  (fillMap2 s m).fst

def isAPaperRoll (m : Std.HashMap (Int × Int) Char) (tuple : Int × Int) :=
  let x := tuple.fst
  let y := tuple.snd
  if m.contains (x,y) && m.get! (x,y) == '@' then
    true
  else
    false

def countNeighbors (m : Std.HashMap (Int × Int) Char) (t : Int × Int) :=
  let x := t.fst
  let y := t.snd
  -- having chosen Nat over Int is BS once again
  ([(x-1, y-1), (x-1, y), (x-1, y+1),
    (x, y-1), (x, y+1),
    (x+1, y-1), (x+1, y), (x+1, y+1)].filter (isAPaperRoll m)).length


def lessThanFour (x : Nat) :=
  -- idk why I may not put a Nat in an Int function
  -- or why Prop is not a Bool
  if x < 4 then
    true
  else
    false

def main : IO Unit := do
  let handle <- Handle.mk "../inputs/input04" .read
  let raw <- handle.readToEnd
  let grid :=  (griddify (String.trim raw))

  let neighbourCount := grid.keys.map (countNeighbors grid)
  -- I made a mistake in mapping EVERY coordinate to something ('@' or '.')
  -- I fixed it by mapping only the paperrols to '@'
  -- this is now a very useless map; but I cannot be bothered to find something to pass the values as well
  let accessible := (neighbourCount.filter lessThanFour).length

  -- no part b now, not in a mood and it's dinner time

  -- this "failed to synthesize" for toString and termination proof business
  -- stinks a lot

  IO.println (grid.size)
  IO.println (accessible)


#eval main
