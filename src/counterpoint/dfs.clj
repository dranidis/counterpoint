(ns counterpoint.dfs
  (:require [counterpoint.intervals :refer [note-at-melodic-interval P4 P5]]
            [counterpoint.notes :as n]))


(defn branch? [node]
  (< (count node) 5))

(defn children [node]
;;   (println "\nNODE" node)
  (if (empty? node)
    [[n/c4]]
    (let [cand (map
                #(note-at-melodic-interval (first node) %)
                [P4 P5])
        ;;   _ (println "\nCAND" cand)
          ]
      (map #(into [%] node) cand))))

(filter #(= (count %) 4) (take 100 (tree-seq branch? children [])))

(tree-seq branch? children [])


