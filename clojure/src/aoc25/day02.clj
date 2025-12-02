(ns aoc25.day02
  (:use [aoc25.commons])
  (:require [clojure.string]))


(def groups (partition 2 (map parse-long (clojure.string/split (clojure.string/trim (read-input "input02")) #",|-"))))
(def expanded (map (fn [[a b]] (range a (inc b))) groups))

(defn invalid? 
  "An ID is invalid (for part 1), if there exists a string x so that (str ID) = (str x x).
   This implementation cuts (str ID) in half and compares both sides."
  [n]
  (let [s (str n)
        m (count s)]
    (when (even? m)
      (= (subs s 0 (/ m 2)) (subs s (/ m 2))))))

(def invalids (filter invalid? (apply concat expanded)))
(reduce + invalids)

;; Part 2
(defn any-invalid? 
  "An ID is invalid (for part 2), if there exists a string x and a number n
   so that (str ID) = (apply str (repeat n x)).
   This implementation brute forces it by testing whether (str ID) can be split
   into strings with size 1, 2, ... (/ (count (str ID)) 2) and whether these strings 
   are the same."
  ([n]
   (let [s (str n)
         m (count s)]
     (any-invalid? s m 1))) ;; start testing with string sizes of 1
  ([s len partition-size]
   (when (<= partition-size (/ len 2)) ;; stop testing if substring becomes too large, such that no repetition is possible
     (if (zero? (mod len partition-size)) ;; only test if substring size is actually a factor
       (or (apply = (partition partition-size s)) ;; actual test
           (recur s len (inc partition-size)))
       (recur s len (inc partition-size))))))

(def invalids2 (filter any-invalid? (apply concat expanded)))
(time (reduce + invalids2)) ; "Elapsed time: 10922.789627 msecs"




;; slightly alternative solution
(def factors (memoize (fn factors ([n] (factors n 1 []))
                                  ([n cur acc] (if (<= cur (/ n 2))
                                                 (if (zero? (mod n cur))
                                                   (recur n (inc cur) (conj acc cur))
                                                   (recur n (inc cur) acc)) 
                                                 acc)))))

(defn any-invalid2? 
  "Same as any-invalid?, but splits the calculation of the factors of the size.
   Make use of memoization for the factors.
   Does not give much (any) benefit over the other solution, but is a bit simpler."
  ([n]
   (let [s (str n)
         m (count s)]
     (any-invalid2? s (factors m)))) ;; retrieve factors instead of enumerating substring sizes
  ([s factors]
   (when (seq factors) 
     (or (apply = (partition (first factors) s))
         (recur s (rest factors))))))

(def invalids3 (filter any-invalid2? (apply concat expanded)))
(time (reduce + invalids3))
