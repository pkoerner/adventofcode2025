(ns aoc25.day09
  (:use [aoc25.commons]))

;; first part is quite straightforward
(def red-tiles (map (fn [s] (read-string (str \[ s \]))) (read-input-by-line "input09")))

;; enumerate all possible rectangle and their sizes
(def rectangles (into {} (for [tile1 red-tiles, tile2 red-tiles :when (not= tile1 tile2)]
                           [#{tile1 tile2} (* (inc (abs (- (first tile1) (first tile2))))
                                              (inc (abs (- (second tile1) (second tile2)))))])))

;; return the largest size
(second (first (sort-by (comp - second) rectangles)))

;; part 2

;; First approach did not make it. It was checking whether some edge of the polygon intersects with the rectangle.
;; However, I learned the hard way the rectangle might be on the "outside" area of the polygon.
;; Well, another solution had to be found.

;; It is important to note that the grid is way too large for enumeration.
;; However, most columns and rows are "filler" and can be neglected.
;; Here, we project the grid to a smaller one that only includes the columns and rows immediately around any red tile.

;; Visualisation in 1-D:
;; Stupid:  "     X--------------X    "
;; Smarter: " X-X "


(def project-x (into {} (map-indexed (fn [idx x] [x idx]) (sort (set (apply concat (for [[x _] red-tiles] [(dec x) x (inc x)])))))))
(def project-y (into {} (map-indexed (fn [idx y] [y idx]) (sort (set (apply concat (for [[_ y] red-tiles] [(dec y) y (inc y)])))))))

(defn project 
  "Projects a red tile to the compressed grid."
  [[x y]]
  [(project-x x) (project-y y)])

(def projected-tiles (map project red-tiles))

(defn draw-lines 
  "Adds the connection between the points [p1 p2] in the second argument
   to the set of points."
  [m [[x1 y1] [x2 y2]]]
  (into m
        (if (= x1 x2)
          (map (fn [y] [x1 y]) (range (min y1 y2) (inc (max y1 y2)))) ;; excluding, because the end point will be excluded next time
          (map (fn [x] [x y1]) (range (min x1 x2) (inc (max x1 x2)))))))

;; With our compressed grid: Explicitly enumerate the polygon.
(def grid (reduce draw-lines #{} (partition 2 1 (cons (last projected-tiles) projected-tiles))))

(def max-x (inc (reduce max (map first projected-tiles))))
(def max-y (inc (reduce max (map second projected-tiles))))

(defn neighbours 
  "Returns the neighbours to the left, right, above and below a given point.
   Depends on the defined max-x and max-y constants."
  [[x y]]
  (filter (fn [[x y]] (and (<= 0 x max-x) (<= 0 y max-y)))
          [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]))

(defn flood
  "Given the polygon (= walls), floods the grid starting from (0,0).
   Returns all flooded cells."
  [walls]
  (loop [seen #{[0 0]}
        open [[0 0]]]
    (if (empty? open)
      seen
      (let [pos (first open)
            nn (neighbours pos)
            next-poss (remove walls (remove seen nn))]
        (recur (into seen next-poss) (into (subvec open 1) next-poss))))))

(def flooded (flood grid))

(defn enumerate-rectangle 
  "Explicitly enumerated all cells contained in a rectangle."
  [[x1 y1] [x2 y2]]
  (for [x (range (min x1 x2) (inc (max x1 x2)))
        y (range (min y1 y2) (inc (max y1 y2)))]
    [x y]))

;; Final step: only keep track of rectangles that are contained in the polygon.
;; I.e.: - project rectangle defining coordinates to compressed grid
;;       - enumerate all points of the projected rectangle
;;       - find out which are contained (= not a single element is flooded)
;;       - return the largest remaining size
(first (sort-by (comp - second)
                (filter (fn [[rr size]] (not (some flooded (enumerate-rectangle (project (first rr)) (project (second rr)))))) rectangles)))
  ;                   |                                            |              L------------------------J project rectangle defining coordinates to compressed grid
  ;                   |                                            |- enumerate all points of the projected rectangle
  ;                   |- find out which are contained (= not a single element is flooded)

(comment
  "Some visualisation for debugging was necessary.
   Bottom right corner was not flooded, as there was no path to it."
  (spit "foo.bar" 
      (clojure.string/join \newline (for [y (range max-y)] (apply str (for [x (range max-x)]
                                                                        (cond (and (grid [x y]) (flooded [x y])) "X"
                                                                              (grid [x y]) "#"
                                                                              (flooded [x y]) "~"
                                                                              :else " ")))))))

