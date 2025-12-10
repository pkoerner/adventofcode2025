(ns aoc25.day10
  (:use aoc25.commons))

;; TODO: document properly

(defn parse-line [line]
  (let [[input goal ops joltages] (re-find #"\[([.#]+)\] (.*) \{(.*)\}" line)]
    {:goal (vec goal), :ops (read-string (str \[ ops \])), :joltages (read-string (str \[ joltages \]))}))

(def machines (map parse-line (read-input-by-line "input10")))

(def off \.)
(def on \#)

(defn apply-operation [configuration operation]
  (reduce (fn [a e] (update a e {off on, on off})) configuration operation))

(defn find-goal [machine]
  (let [initial-configuration (vec (repeat (count (:goal machine)) off))]
    (loop [configurations [initial-configuration]
           seen #{initial-configuration}
           depth 0]
      (if (some #(= (:goal machine) %) configurations)
        depth
        (let [next-configurations (mapcat (fn [config] (map (partial apply-operation config) (:ops machine))) configurations)
              unseen (remove seen (dedupe next-configurations))]
          (recur unseen (into seen unseen) (inc depth)))))))

; (reduce + (map find-goal machines))

;; part 2
;; time to take out the bigger guns
;; solution actually in lisb / B / ProB

(use 'lisb.core)
(use 'lisb.translation.util)

(defn generate-machine [[constants properties]]
  (str "MACHINE dontcare
                         DEFINITIONS
                          SET_PREF_TIMEOUT == 120000;
                          SET_PREF_SOLVER_FOR_PROPERTIES == \"z3\" "
         (ir->b constants) " "
         (ir->b properties)
         " END"))

(defn machine->constraints [machine curmax] 
  (let [v (vec (repeat (count (:joltages machine)) []))
        ids (map (fn [_] (keyword (gensym "wire"))) (:ops machine))
        vargroups (reduce (fn [a [wiring id]] 
                                           (reduce (fn [a' wire] (update a' wire (fn [v] (conj v id)))) a wiring)) 
                          v
                          (map vector (:ops machine) ids))]
    [(apply bconstants :res ids)
     (bproperties 
       (b= :res (apply b+ ids))
       (b< :res curmax)
       (apply band 
              (concat (map #(b<= 0 % (reduce max (:joltages machine))) ids)
                      (map (fn [vars goal] (b= (apply b+ vars) goal)) vargroups (:joltages machine)))))]))

  (import 'de.prob.animator.domainobjects.FormulaExpand)
  (def FORMULA-EXPANSION FormulaExpand/EXPAND)

  (defn solve-machine 
    ([machine] (solve-machine machine (reduce + (:joltages machine))))
    ([machine curmax]
      (let [ss (ir-state-space!  (b->ir (generate-machine (machine->constraints machine curmax))))
            op (.findTransition (.getRoot ss) "$setup_constants" [])
            constants (when op (.getConstantValues  (.getDestination op) FORMULA-EXPANSION))
            res (when constants (parse-long (.getValue (val (first (filter (fn [x] (= "res" (.getCode (key x)))) constants))))))]
        (.kill ss)
        (if res 
          (solve-machine machine res)
          curmax
          )
        )))

(reduce + (map solve-machine machines))

