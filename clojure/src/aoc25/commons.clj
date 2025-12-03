(ns aoc25.commons
  (:require [clojure.string]))

(defn read-input-by-line [f]
  (clojure.string/split-lines (clojure.string/trim (slurp (str "../inputs/" f)))))

(defn read-input [f]
  (clojure.string/trim (slurp (str "../inputs/" f))))

(def chardigit->num
  {\0 0 \1 1 \2 2 \3 3 \4 4 \5 5 \6 6 \7 7 \8 8 \9 9})
