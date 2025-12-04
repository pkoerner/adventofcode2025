(ns aoc25.day04
  (:use aoc25.commons)
  (:require [clojure.set]))

;; My favorite representation of 2D grid in Clojure:
;;   map {:x x, :y y} to the symbol/value at (x,y)
;; Here, we only have one kind, so we do not need the value 
;; and can store the keys (that is, the {:x x, :y y} locations) in a set.
(def paperrolls (set (keys (read-grid (read-input-by-line "input04") \.))))

(defn neighbours-8
  "Takes a map of the form {:x x, :y y} and will return the adjacent
  (horizontal, vertical, diagonal) locations.
  Does not exclude values outside of the grid, 
  e.g., {:x -1, :y -1} will be generated."
  [{:keys [x y] :as pos}]
  (for [xx [(dec x) x (inc x)]
        yy [(dec y) y (inc y)]
        :when (not (and (= xx x) (= yy y)))]
    {:x xx, :y yy}))

(defn accessible? 
  "Determines whether a specific roll in a set of paperrolls is accessible."
  [paperrolls roll]
  ;; generate the neighbours, filter which are actual paperrolls, count them
  (< (count (filter paperrolls (neighbours-8 roll))) 4))

(count (filter (partial accessible? paperrolls) paperrolls))


;; part 2

(defn remove-rolls
  "Iteratively removes accessible rolls from the passed set of paperrolls
  Returns the number of paperrolls removed in this fashion."
  [rolls]
  (loop [rolls-cur rolls
         n-removed 0]
    (let [accessible (set (filter (partial accessible? rolls-cur) rolls-cur)) ;; same as before
          n-accessible (count accessible)
          rolls-next (clojure.set/difference rolls-cur accessible)] ;; just... remove accessible ones
      ;; A little trick would be to test for (empty? ...) instead of (zero? ...),
      ;; and just return (- (count rolls) (count rolls-cur)).
      ;; But this is probably a tiny bit easier to follow.
      (if (zero? n-accessible) ;; done if nothing can be removed
        n-removed
        (recur rolls-next (+ n-removed n-accessible)))))) ;; keep track

(remove-rolls paperrolls)
