(ns counterpoint.gen-fifth-species
  (:require [clojure.java.shell :as sh]
            [counterpoint.elaborations :refer [elaborate-4th]]
            [counterpoint.fifth-species :refer [evaluate-fifth-species
                                                fifth-species-rules?
                                                make-fifth-species]]
            [counterpoint.gen-first-dfs :refer [last-note-candidates]]
            [counterpoint.gen-fourth-dfs :refer [next-reverse-candidates-4th
                                                 second-to-last-measure-candidates-4th]]
            [counterpoint.gen-third-dfs :refer [next-reverse-candidates-3rd]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-first-species :refer [update-m36-size]]))

(defn candidates [{:keys [position
                          key
                          melody
                          bar-melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]
                   :as state}]
  ;; (println "(REV) MELODY" melody)
  ;; (println "(BAR) MELODY" bar-melody)

  (case (count bar-melody)
    0 (map (fn [n] [{:d 1 :n [n]}])
           (last-note-candidates position (first cantus-notes) cantus-note))
    1 (map (fn [n] (elaborate-4th state [{:d 2 :n n}]))
           (second-to-last-measure-candidates-4th
            position key previous-melody previous-cantus
            cantus-note (first cantus-notes)))
    (map (fn [n] (elaborate-4th state [{:d 2 :n n}]))
           (next-reverse-candidates-4th state))
    ;; (map (fn [n] [{:d 2 :n n}]) (next-reverse-candidates-4th state))
    ))


;; (apply concat (map #(get % :n) [{:n [1 2]} {:n [3 4]}]))

(defn- next-node-fifth [{:keys [position
                                key
                                melody
                                bar-melody
                                m36s ;; counter of thirds & sixths
                                previous-melody
                                previous-cantus
                                cantus-note
                                cantus-notes]}
                        current]
  ;; (println "CURRENT" current)
  ;; (println "BAR MELODY" bar-melody)
  ;; (println "NEW BAR MELODY" (conj bar-melody current))
  (let [current-melody (apply concat (map #(get % :n) current))]
    {:position position
     :key key
     :melody (into melody current-melody)
     :bar-melody (conj bar-melody current)
     :m36s (update-m36-size m36s position cantus-note current-melody)
     :previous-melody (last current-melody)
     :previous-cantus cantus-note
     :cantus-note (first cantus-notes)
     :cantus-notes (rest cantus-notes)}))

(def generate-fifth
  (generate-template
   candidates
   evaluate-fifth-species
   make-fifth-species
   fifth-species-rules?
   next-node-fifth))

(defn play-best-fifth [n cf position]
  (generate-fifth n cf position
                  {:pattern ""
                   :midi "acoustic grand"})
  (sh/sh "timidity" "resources/temp.midi"))

