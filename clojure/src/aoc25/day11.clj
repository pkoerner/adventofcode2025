(ns aoc25.day11
  (:use aoc25.commons))

;; build graph data structure: map from devices to successor devices
(def g (into {} (map (fn [s] (let [[h & t] (clojure.string/split s #": | ")] [h t])) (read-input-by-line "input11"))))

(def pathes-to-out
  "Memoized function. Given a graph and a start node,
   calculates the number of pathes from that start node to out."
  (memoize (fn [g s]
             ;; basic idea: out -> out is one possible path (staying there)
             ;; otherwise, we recurse: find all successors, how many pathes are possible from them, sum up
             ;; memoization might be vital, as successors may be reached from different pathes 
             ;; and we do not to re-calculate the same information
             (if (= "out" s)
               1
               (reduce + (map (partial pathes-to-out g) (get g s)))))))

(pathes-to-out g "you")

;; part 2
(def pathes-to-out2
  "Memoized function. Given a graph and a start node,
   returns a map {:both n1, :dac n2, :fft n3, :neither n4}.
   It indicates the number of pathes from that start node to out
   that (1) pass through *both* the 'dac' and 'fft' node,
        (2) pass through the *dac* node, but not the 'fft' node,
        (3) pass through the *fft* node, but not the 'dac' node,
        (4) pass through *neither* the 'dac' nor the 'fft' node."
  (memoize (fn [g s]
             (if (= "out" s)
               ;; from out, we still get to out via one possible path (staying there)
               ;; on this path, we pass neither the dac nor the fft node
               {:neither 1}
               ;; recurse over successors, merge the maps containing the results with addition
               (let [xx (apply (partial merge-with +) (map (partial pathes-to-out2 g) (get g s)))]
                 (case s
                   ;; if we are the dac node: 
                   ;;    if we have seen the fft node, we have seen both relevant nodes
                   ;;    if we have seen neither, we have at least seen the dac node
                   ;; if we are the fft node: analogously to the above.
                   "dac" {:both (get xx :fft 0), :dac (get xx :neither 0)}
                   "fft" {:both (get xx :dac 0), :fft (get xx :neither 0)}
                   ;; otherwise, nothing special happens
                   xx))))))

(:both (pathes-to-out2 g "svr"))
