(ns aoc25.day03
  (:use aoc25.commons))


(def inputdata (map (fn [x] (map chardigit->num x)) (read-input-by-line "input03")))

;; basic idea: start with the least significant digits, scan from right to left
;; if I switch the more significant one:
;;   either the more significant digit is the local max and the less significant can adapt this value
;;   or the less significant digit is bigger and can discard it
;; if the more significant digit is larger than the currently considered value, nothing can change
(defn get-highest-two-digit 
  "Extracts the two digits from cc which (in order of their occurence) form the largest possible number."
  [cc]
  (loop [c (drop 2 (reverse cc))
        more-significant (second (reverse cc))
        less-significant (first (reverse cc))]
    (if (seq c)
      (recur (rest c)
             (max more-significant (first c)) ;; may switch first digit
             (if (<= more-significant (first c))
               (max less-significant more-significant) ;; if switched first digit: might switch second digit to prior max
               less-significant)) ;; or not
      (+ (* 10 more-significant) less-significant))))

(reduce + (map get-highest-two-digit inputdata))

;; part b

;; reshuffling could be optimised: if the condition is hit once, the rest could just be copied
(defn reshuffle 
  "'Offers' a number n to a collection of numbers.
   If n is bigger than the first element of the collection,
   will replace it. In turn, this element is offered in the same way to the rest of the collection."
  [c n]
  (first (reduce (fn [[acc cand] e]
                   (if (>= cand e)
                     [(conj acc cand) e] ;; switch, release current value
                     [(conj acc e) -1]))  ;; stay, no new munch
                 [[] n]
                 c)))

;; more abstract version of the same idea above:
;; start with the n least significant digits, scan from right to left
;; reshuffling works as follows:
;;   either the most significant digit can switch (and "release" its current max to the less significant)
;;   or it cannot change and offers -1, which is less than the current value
(defn get-highest-n-digits 
  "Extracts the n digits from cc which (in order of their occurence) form the largest possible number."
  [n cc]
  (loop [c (drop n (reverse cc))
         res (take-last n cc)]
    (if (seq c)
      (recur (rest c)
             (reshuffle res (first c)))
      (parse-long (apply str res)))))



(reduce + (map (partial get-highest-n-digits 12) inputdata))

(comment 
  (use 'testor.core)

  (fixate-all!!
  (get-highest-two-digit (map chardigit->num "12"))
  (get-highest-two-digit (map chardigit->num "12"))
  (get-highest-two-digit (map chardigit->num "8143219")) 
  (get-highest-two-digit (map chardigit->num "8143219"))
  (get-highest-two-digit (map chardigit->num "987654321111111"))
  (get-highest-two-digit (map chardigit->num "811111111111119"))
  (get-highest-two-digit (map chardigit->num "234234234234278"))
  (get-highest-two-digit (map chardigit->num "987654321111111"))
  (get-highest-two-digit (map chardigit->num "111112112"))
  (get-highest-two-digit (map chardigit->num "818181998111")) ;; initially, I had a bug where I compared < instead of <=, resulting in 98 instead of 99. This is a relevant test case.
  (get-highest-two-digit (map chardigit->num "818181911112111"))
  (get-highest-n-digits 12 (map chardigit->num "987654321111111"))
  (get-highest-n-digits 12 (map chardigit->num "811111111111119"))
  (get-highest-n-digits 12 (map chardigit->num "234234234234278"))
  (get-highest-n-digits 12 (map chardigit->num "818181911112111"))
  (get-highest-n-digits 12 (map chardigit->num "8189999999999911111111")))

  ;; some manually comparing values to find aforementioned bug
  (spit "foo" (clojure.string/join \newline (map #(clojure.string/join " " %) (map vector (map (partial apply str) inputdata) (map get-highest-two-digit inputdata))))) ;; too low 17004
  )
