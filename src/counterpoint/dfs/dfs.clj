(ns counterpoint.dfs.dfs
  (:require [counterpoint.intervals :refer [note-at-melodic-interval P4 P5]]
            [counterpoint.notes :as n]))


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
