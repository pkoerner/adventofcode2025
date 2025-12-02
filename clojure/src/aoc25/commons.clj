(ns aoc25.commons
  (:require [clojure.string]))

(defn read-input-by-line [f]
  (clojure.string/split-lines (slurp (str "../inputs/" f))))

(defn read-input [f]
  (slurp (str "../inputs/" f)))
