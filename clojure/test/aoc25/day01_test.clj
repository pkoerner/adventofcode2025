(ns
 aoc25.day01-test
 (:require [clojure.test :refer :all] [aoc25.day01 :refer :all]))

(deftest
 safe-step-interp-test-1764577337-2609
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 99 {:direction :right, :distance 1}) 0))))

(deftest
 safe-step-interp-test-1764577337-2612
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 99 {:direction :right, :distance 2}) 1))))

(deftest
 safe-step-interp-test-1764577337-2615
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 10 {:direction :right, :distance 5}) 15))))

(deftest
 safe-step-interp-test-1764577337-2618
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 99 {:direction :left, :distance 9}) 90))))

(deftest
 safe-step-interp-test-1764577337-2621
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 10 {:direction :left, :distance 5}) 5))))

(deftest
 safe-step-interp-test-1764577337-2624
 (testing
  "this was deemed correct during development"
  (is (= (safe-step-interp 2 {:direction :left, :distance 3}) 99))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2928
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 10})
    {:dial 60, :zeroes 0}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2931
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 49})
    {:dial 99, :zeroes 0}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2934
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 50})
    {:dial 0, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2937
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 51})
    {:dial 1, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2940
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :left, :distance 10})
    {:dial 40, :zeroes 0}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2943
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :left, :distance 49})
    {:dial 1, :zeroes 0}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2946
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :left, :distance 50})
    {:dial 0, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2949
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :left, :distance 51})
    {:dial 99, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2952
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 100})
    {:dial 50, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2955
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     {:direction :right, :distance 1000})
    {:dial 50, :zeroes 10}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2958
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 0, :zeroes 1}
     {:direction :right, :distance 50})
    {:dial 50, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2961
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 0, :zeroes 1}
     {:direction :left, :distance 50})
    {:dial 50, :zeroes 1}))))

(deftest
 safe-interp-count-zeroes-test-1764583260-2964
 (testing
  "this was deemed correct during development"
  (is
   (=
    (safe-interp-count-zeroes
     {:dial 0, :zeroes 0}
     {:direction :left, :distance 100})
    {:dial 0, :zeroes 1}))))

(deftest
 reductions-test-1764583260-2967
 (testing
  "this was deemed correct during development"
  (is
   (=
    (reductions
     safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     test-seq)
    [{:dial 50, :zeroes 0}
     {:dial 82, :zeroes 1}
     {:dial 52, :zeroes 1}
     {:dial 0, :zeroes 2}
     {:dial 95, :zeroes 2}
     {:dial 55, :zeroes 3}
     {:dial 0, :zeroes 4}
     {:dial 99, :zeroes 4}
     {:dial 0, :zeroes 5}
     {:dial 14, :zeroes 5}
     {:dial 32, :zeroes 6}]))))

(deftest
 =-test-1764583260-2970
 (testing
  "this was deemed correct during development"
  (is
   (=
    {:dial 22, :zeroes 5899}
    (reduce
     safe-interp-count-zeroes
     {:dial 50, :zeroes 0}
     input-seq)))))
