(ns counterpoint.dfs.dfs
  (:require [counterpoint.intervals :refer [note-at-melodic-interval P4 P5]]
            [counterpoint.notes :as n]))

(def length 10)

(def root-node [])


(defn solution? [node]
  (= (count node) length))

;; does the node have children
;; (defn branch? [node]
;;   (< (count node) length))

;; next node after cand
(defn next-node [node cand]
  (into [cand] node))

(defn candidates [node]
  (if (= node root-node)
    [n/c4]
    (map
     #(note-at-melodic-interval (first node) %)
     [P4 P5])))

;; list of children of the node
;; (defn children [node]
;;   (map #(next-node node %) (candidates node)))

(defn generate-dfs-solutions
  "Arguments:
   - root-node node representing root state
   - candidates is a function receiving a state node and returning a list of 
   possible actions
   - next-node is a function receiving a state node and an action and returns the next state node
   - solution? is a predicate receiving a state node returing true if the state node is a leaf (solution)"
  [root-node candidates next-node solution?]
  (let [children (fn [node]
                   (map #(next-node node %) (candidates node)))
        branch? (fn [node] (not (solution? node)))]
    (filter solution?
            (tree-seq branch? children root-node))))

(def all-solutions (generate-dfs-solutions [] candidates next-node solution?))

(first all-solutions)
;; (last complete)
;; (drop 1 complete)



(def water-root {:8 0 :5 0 :actions [] :visited #{}})

(defn water-solution? [state]
  ;; (println "IS SOLUTION " state)
  (let [solution? (or (= (get state :8) 1)
      (= (get state :5) 1))]
    ;; (println solution?)
    solution?))

(defn water-candidates [state]
  ;; (println "AT STATE" state)
  (let [visited (get state :visited)
        jug-state {:8 (get state :8) :5 (get state :5)}
        cand (if (visited jug-state)
               []
               [:fill-1 :empty-1 :fill-2 :empty-2 :pour-1-2 :pour-2-1])]
    ;; (println "  CANDIDATES" cand)
    cand))

(defn water-next [state action]
  ;; (println "FROM" state "\n  WITH ACTION" action)
  (let [new-state (case action
                    :fill-1 (assoc state :8 8)
                    :empty-1 (assoc state :8 0)
                    :fill-2 (assoc state :5 5)
                    :empty-2 (assoc state :5 0)
                    :pour-1-2 (let [jug1 (get state :8)
                                    jug2 (get state :5)
                                    rest-jug2 (- 5 jug2)
                                    pour-from-jug1 (if (> jug1 rest-jug2)
                                                     rest-jug2
                                                     jug1)
                                    new-jug1 (- jug1 pour-from-jug1)
                                    new-jug2 (+ jug2 pour-from-jug1)]
                                (assoc (assoc state :8 new-jug1) :5 new-jug2))
                    :pour-2-1 (let [jug1 (get state :8)
                                    jug2 (get state :5)
                                    rest-jug1 (- 8 jug1)
                                    pour-from-jug2 (if (> jug2 rest-jug1)
                                                     rest-jug1
                                                     jug2)
                                    new-jug2 (- jug2 pour-from-jug2)
                                    new-jug1 (+ jug1 pour-from-jug2)]
                                (assoc (assoc state :8 new-jug1) :5 new-jug2)))
        return (update (update new-state :visited #(conj % {:8 (get state :8)
                                                            :5 (get state :5)}))
                       :actions #(conj % action))]
    ;; (println "  NEW STATE" return)
    return))

(-> water-root
    (water-next :fill-1)
    (water-next :fill-2)
    (water-next :empty-1)
    (water-next :pour-2-1)
    (water-next :fill-2)
    (water-next :pour-2-1))

(take 1 (generate-dfs-solutions water-root water-candidates water-next water-solution?))








