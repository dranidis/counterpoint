(ns counterpoint.gen-third-dfs
  (:require [counterpoint.core :refer [simple-interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.gen-first-dfs :refer [last-note-candidates solution?]]
            [counterpoint.gen-second-dfs :refer [next-node]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.intervals :refer [harmonic-consonant?]]
            [counterpoint.notes :refer [get-note]]
            [counterpoint.third-species :refer [evaluate-third-species
                                                make-third-species
                                                third-species-rules?]]
            [counterpoint.third-species-patterns :refer [ending-1 ending-1-e
                                                         ending-2 ending-below
                                                         ending-below-e patterns pattern-fits?]]))

(defn- second-to-last-measure-candidates-3rd
  [{:keys [position key previous-melody previous-cantus cantus-note]}]
  (if (= position :above)
    (if (= :e (get-note previous-melody))
      [ending-1-e]
      [(ending-1 key previous-melody)
       (ending-2 key previous-melody)])
    (if (= :e (get-note previous-melody))
      [ending-below-e]
      [ending-below])))

;; (defn- next-reverse-candidates-3rd
;;   [{:keys [position key previous-melody cantus-note]}]
;;   (->> (map #(% key previous-melody) patterns)
;;        (filter (fn [[p4 p3 p2 p1]]
;;                  (harmonic-consonant?
;;                   (simple-interval cantus-note p1 position))))))

(defn- next-reverse-candidates-3rd
  [state]
  (reduce (fn [candidates pattern]
            (let [[applied-patern fits?] (pattern-fits? pattern state)]
              (if fits?
              (into candidates [applied-patern])
              candidates)))
          []
          patterns))

(defn candidates [{:keys [position
                          key
                          melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]
                   :as state}]
  (case (count melody)
    0 (map (fn [n] [n]) (last-note-candidates position cantus-note))
    1 (second-to-last-measure-candidates-3rd state)
    (next-reverse-candidates-3rd state)))

(defn generate-reverse-counterpoint-3rd-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
        melody []
        previous-melody nil
        previous-cantus nil
        root-node {:position position
                   :key key
                   :melody melody
                   :m36s m36s;; counter of thirds & sixths
                   :previous-melody previous-melody
                   :previous-cantus previous-cantus
                   :cantus-note (first rev-cantus)
                   :cantus-notes (rest rev-cantus)}]
    (generate-dfs-solutions root-node candidates next-node solution?)))

(def generate-third
  (generate-template
   generate-reverse-counterpoint-3rd-dfs
   evaluate-third-species
   make-third-species
   third-species-rules?))