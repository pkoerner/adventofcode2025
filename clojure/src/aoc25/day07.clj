(ns aoc25.day07
  (:use aoc25.commons))

(def input-lines (read-input-by-line "input07"))
(def input-grid (read-grid input-lines \.))
(def max-y (count input-lines))

(defn find-emitter 
  "Locates the emitter of tachyon beams 'S' in the grid."
  [input-grid]
  (ffirst (filter (fn [[pos sym]] (= sym \S)) input-grid)))

(defn move-tachyon
  "Given a grid and a current position of a tachyon,
   moves the tachyon downwards. Returns a sequence of all possible
   positions it could be in (either moved directly down, or split in two)."
  [input-grid {:keys [x y] :as pos}]
  (let [next-pos (update pos :y inc)] ;; move down
    (if (= \^ (get input-grid next-pos)) ;; split
      [(update next-pos :x dec) (update next-pos :x inc)]
      ;    move down-left            move down-right
      [next-pos])))

(defn move-tachyon-beams 
  "Simulates the tachyon beam emitted from the starting location.
   Keeps track of how many splits occur."
  [input-grid emitter]
  (loop [tachyons #{emitter}
         y 0
         splits 0]
    (if (< max-y y)
      splits
      (let [next-tachyons (mapcat (partial move-tachyon input-grid) tachyons)] ;; this includes duplicates
        (recur (set next-tachyons) ;; remove duplicates, treat as one
               (inc y)
               (+ splits (- (count next-tachyons) (count tachyons)))))))) ;; count the number of new tachyon locations (= number of split)

(move-tachyon-beams input-grid (find-emitter input-grid))


;; part 2
(defn calculate-timelines 
  "Simulates the tachyon beam emitted from the starting location.
   Keeps track of how many timelines are possible."
  [input-grid emitter]
  (loop [tachyons {emitter 1} ;; at the emitter, the location can be reached in one way
         y 0]
    (if (< max-y y)
      (reduce + (vals tachyons))
      ;; this thing is a bit tricky and a long line with 4 HOFs
      ;; basically, we keep track of a map that maps locations to the number of ways they can be reached
      ;; due to splits, locations can converge again. Then, we have have to add up different way to reach the same location.
      ;; the amount of possible ways to reach any position in the last row is the number of possible timelines
      (let [next-tachyons (apply merge-with + (map (fn [[pos n]] (into {} (map (fn [pos'] [pos' n]) (move-tachyon input-grid pos)))) tachyons))]
        ;;                              |                           |       |- copy the number of ways that successors can be reached (as either side of a split is fine)
        ;;                              |                           - transform back into a map
        ;;                              - merge together maps caused by tachyons that were in different locations but (partially) ended up in the same location
        (recur (set next-tachyons) (inc y))))))

(calculate-timelines input-grid (find-emitter input-grid))
