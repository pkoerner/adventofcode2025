(ns aoc25.day01
  (:use [aoc25.commons]))

(defn datafy-input 
  "Transforms a string-instruction such as 'L10' into a map
   of the form {:direction :left, :distance 10}."
  [s]
  {:direction ({\R :right \L :left} (first s))
   :distance (parse-long (apply str (rest s)))})

(def test-seq (map datafy-input ["L68" "L30" "R48" "L5" "R60" "L55" "L1" "L99" "R14" "L82" ]))
(def input-seq (map datafy-input (read-input-by-line "input01")))

(defn safe-step-interp 
  "*Interp*rets one instruction of the form
   {:direction :left/right, :distance 42} on the safe's
   current dial value, such as 50."
  [dial instr]
  (mod (({:left -, :right +} (:direction instr))
        dial
        (:distance instr)) 
       100))

(comment
  "I use such rich comment blocks in order to test manually what I did during development.
   I also use my testor library to add these executions as automatic tests.
   This can be skipped."
  (use 'testor.core)
  (fixate-all!!
    (safe-step-interp 99 {:direction :right, :distance 1})  
    (safe-step-interp 99 {:direction :right, :distance 2})  
    (safe-step-interp 10 {:direction :right, :distance 5})
    (safe-step-interp 99 {:direction :left, :distance 9})  
    (safe-step-interp 10 {:direction :left, :distance 5})
    (safe-step-interp 2 {:direction :left, :distance 3})))

;; answer part 1
;; Puzzle text: The actual password is the number of times the dial is left pointing at 0 after any rotation in the sequence.
;; Use reductions to get the values after any rotation.
;; Use filter zero? to extract the dial values equal to zero.
;; count the instances of 0 to get to the solution.
(count (filter zero? (reductions safe-step-interp 50 input-seq)))



(defn safe-interp-count-zeroes 
  "Similar to safe-step-interp, but also keeps track of the
   number of times the dial points at zero *during* a rotation.
   The dial state is represented as {:dial 50, :zeroes 0}."
  [state instr]
  ;; NOTE: I hate this, this is ugly, this is error-prone, I do not understand it entirely;
  ;; - but: it is faster.
  (let [extra-rotations (quot (:distance instr) 100) ;; keep track of full rotations
        remaining-distance (mod (:distance instr) 100)
        crosses-or-touches-zero (and (not (zero? (:dial state))) ;; if we do a full rotation, it is already accounted for by the extra rotations
                                     (or (and (= :left (:direction instr))
                                              (>= remaining-distance (:dial state))) ;; do we hit 0 turning left?
                                         (and (= :right (:direction instr))
                                              (>= remaining-distance (- 100 (:dial state))))))] ;; do we hit 0 turning right?
    (-> state
        (update :dial safe-step-interp instr)
        (update :zeroes + extra-rotations (if crosses-or-touches-zero 1 0)))))

(defn expand-instr 
  "Instead of turning the dial for the distance n at once,
   turn the dial n times for distance 1.
   Used to debug the mess above."
  [instr]
  (repeat (:distance instr) (assoc instr :distance 1)))

;; two ways to get to the answer
(:zeroes (reduce safe-interp-count-zeroes {:dial 50, :zeroes 0} input-seq))
(reduce safe-interp-count-zeroes {:dial 50, :zeroes 0} (mapcat expand-instr input-seq))

;; Because I did not get my logic correct in a few attempts: here is my debugging method.
;; - transform an instruction {:direction :right, :distance 3} into 3 instances of {:direction :right, :distance 1},
;; - compare executing the single instruction with executing the expanded form
;; - see where it went wrong and fix the code accordingly.

(comment 
  (let [a (reductions safe-interp-count-zeroes {:dial 50, :zeroes 0} input-seq)
        b (reductions (fn [a eseq] (reduce safe-interp-count-zeroes a eseq)) {:dial 50, :zeroes 0} (map expand-instr input-seq))
        good-prefix (take-while (partial apply =) (map vector a b))]
    [(last good-prefix) (count good-prefix)]))

(comment
  (use 'testor.core)
  (fixate-all!!
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 10})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 49})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 50})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 51})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :left, :distance 10})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :left, :distance 49})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :left, :distance 50})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :left, :distance 51})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 100})
    (safe-interp-count-zeroes {:dial 50, :zeroes 0} {:direction :right, :distance 1000})
    (safe-interp-count-zeroes {:dial 0, :zeroes 1} {:direction :right, :distance 50})
    (safe-interp-count-zeroes {:dial 0, :zeroes 1} {:direction :left, :distance 50})
    (safe-interp-count-zeroes {:dial 0, :zeroes 0} {:direction :left, :distance 100})
    (reductions safe-interp-count-zeroes {:dial 50, :zeroes 0} test-seq)
    ; (= {:dial 22, :zeroes 5899} (reduce safe-interp-count-zeroes {:dial 50, :zeroes 0} (mapcat expand-instr input-seq)))
    (= {:dial 22, :zeroes 5899} (reduce safe-interp-count-zeroes {:dial 50, :zeroes 0} input-seq))
       )
  )
