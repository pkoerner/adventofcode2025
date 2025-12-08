(ns aoc25.day08
  (:use aoc25.commons)
  (:require [clojure.data.priority-map :refer [priority-map]]
            [clojure.set]))

(def box-locations (map (fn [s] (read-string (str \[ s \]))) (read-input-by-line "input08")))

(defn square
  "Multiplies a value with itself."
  [x] 
  (* x x))

(defn distance 
  "Takes two 3-dimensional points and calculcates their Euclidean distance."
  [[x1 y1 z1] [x2 y2 z2]]
  (Math/sqrt (+ (square (- x1 x2))
                (square (- y1 y2))
                (square (- z1 z2)))))

(def precalculated-distances (into (priority-map)
                                   (for [box1 box-locations
                                         box2 box-locations
                                         :when (not= box1 box2)]
                                     [#{box1 box2} (distance box1 box2)])))

(def circuit-by (into {} (map (fn [box] [box #{box}]) box-locations)))

(defn add-to-all 
  "Takes a map of object to set and two sets.
  Will add the second set to the value of all map entries contained in the first set."
  [m s1 s2]
  (reduce (fn [m e] (update m e clojure.set/union s2)) m s1))

(defn connect
  "Given a map of current circuits and two boxes, will 
  return the circuit where the boxes are connected."
  [circuits box1 box2]
  (let [circuit1 (get circuits box1)
        circuit2 (get circuits box2)]
    ;; performance optimisation: skip adding to circuit if already connected
    (if (get circuit1 box2)
      circuits ;; already connected
      (-> circuits ;; update all circuit1 w/ union circuit2, vice versa
          (add-to-all circuit1 circuit2)
          (add-to-all circuit2 circuit1)))))

(let [closest1000 (take 1000 (seq precalculated-distances))
      new-circuits (reduce (fn [a [e _dist]] (connect a (first e) (second e))) circuit-by closest1000)]
  (apply * (map count (take 3 (sort-by (comp - count) (set (vals new-circuits)))))))
;                        |                              |    L all circuits
;                        |                              |- unique circuits
;                        |- three largest circuits

;; part 2
(let [new-circuits (reduce (fn [a [e _dist]] 
                             (let [a' (connect a (first e) (second e))]
                               (if (= (count box-locations) (count (val (first a')))) 
                                 ;; once ANY circuit contains ALL boxes, everything is connected.
                                 (reduced e) ;; last connection is made to complete the circuit. return this.
                                 a'))) 
                           circuit-by 
                           precalculated-distances)]
  (apply * (map first new-circuits)))
