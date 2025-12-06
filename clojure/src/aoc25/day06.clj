(ns aoc25.day06
  (:use aoc25.commons))

(def raw-input (read-input-by-line "input06"))

(def input (map (fn [x] (read-string (str \[ x \]))) (read-input-by-line "input06")))

;; default trick: just do (apply map vector ...) to transpose the thing
;; then, put the operator in front, just for readability :-)
;; would not matter to extract it here or later
(def reordered (map (fn [x] (cons (last x) (butlast x))) (apply map vector input)))

(defn evaluate
  "Evaluates an expression of the form ('+ 1 2 3) or ('* 42 1337).
   Note that only + or * are supported, and both have to be passed as symbols."
  [[op & nums]]
  (cond (= op '*) (apply * nums)
        (= op '+) (apply + nums)))

(reduce + (map evaluate reordered))

;; part b
(defn extract-problems-from-input
  "Extracts the semi-parsed problems from the input.
   Opposed to just reading strings, keeps track of whitespaces."
  [s]
  (loop [operators (last s)
         nums (butlast s)
         acc []]
    (let [len (count (take-while (partial = \space) (rest operators)))]
      (if (pos? len)
        (recur (drop (inc len) operators)
               (map #(drop (inc len) %) nums)
               (conj acc (cons ({\+ '+, \* '*} (first operators))
                               (map #(take len %) nums))))
        (conj acc (cons ({\+ '+, \* '*} (first operators))
                         nums))))))

(defn reorder-digits
  "Takes a semi-parsed problem and transforms the digits from human to cephalod math."
  [[op & problem]]
  (cons op (map (comp parse-long clojure.string/trim (partial apply str)) (apply map vector problem))))

(reduce + (map evaluate (map reorder-digits (extract-problems-from-input raw-input))))
