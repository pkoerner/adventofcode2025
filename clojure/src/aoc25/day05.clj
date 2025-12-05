(ns aoc25.day05
  (:use aoc25.commons))


(defn parse-range
  "Transforms string s of the form '123-4567' into a map {:from 123, :to 4567}."
  [s]
  (let [[_ from to] (re-find #"(\d+)-(\d+)" s)]
    {:from (parse-long from), :to (parse-long to)}))

(def input-lines (read-input-by-line "input05"))
(def ranges (map parse-range (take-while seq input-lines)))
(def ingredients (map parse-long (reverse (take-while seq (reverse input-lines)))))

(defn in-range? 
  "Tests whether the number n is included in the range 'interval'."
  [{:keys [from to] :as interval} n]
  (<= from n to))

(count (filter (fn [ingredient] (some #(in-range? % ingredient) ranges)) ingredients))

;; part 2

(defn range-merge 
  "Takes a collection of ranges, sorted by their corresponding start.
   Will merge ranges that overlap and are 'next to each other' (consecutive) in the input sequence."
  ([ranges] (range-merge [] (first ranges) (rest ranges)))
  ([acc cur ranges]
   (if (empty? ranges)
     (conj acc cur)
     (let [next-range (first ranges)]
       (if (in-range? cur (:from next-range))
         (recur acc (update cur :to max (:to next-range)) (rest ranges))
         (recur (conj acc cur) next-range (rest ranges)))))))

(defn range-size 
  "Takes a range as argument and returns the amount of integer numbers contained in it."
  [interval]
  (inc (- (:to interval) (:from interval))))

(reduce + (map range-size (range-merge (sort-by :from ranges))))

