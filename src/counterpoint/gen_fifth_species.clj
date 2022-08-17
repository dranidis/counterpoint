(ns counterpoint.gen-fifth-species
  (:require [clojure.java.shell :as sh]
            [counterpoint.elaborations :refer [elaborate-4th
                                               elaborate-suspension-with-next-working-bar]]
            [counterpoint.fifth-species :refer [evaluate-fifth-species
                                                fifth-species-rules?
                                                make-fifth-species]]
            [counterpoint.gen-first-dfs :refer [last-note-candidates]]
            [counterpoint.gen-fourth-dfs :refer [next-reverse-candidates-4th
                                                 second-to-last-measure-candidates-4th]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-first-species :refer [update-m36-size]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-position]]
            [counterpoint.utils :refer [reverse-bar-melody]]))

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
    1 (map (fn [n]  [{:d 2 :n n}])
           (second-to-last-measure-candidates-4th
            position key previous-melody previous-cantus
            cantus-note (first cantus-notes)))
    (map (fn [n] [{:d 2 :n n}])
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

(defn- elaborate-bar-melody-iter
  [current-working-bar rest-working-bars
   {:keys [bar-melody key position previous-melody cantus-note rest-cantus]
    :as state}]
  ;; (println "CANTUS NOTE" cantus-note)
  ;; (println "BAR-MELODY" bar-melody)
  ;; (println "Working bar" current-working-bar)
  ;; (println "Prev melody" previous-melody)
  ;; (println "elaborate-bar-melody-iter KEY" key)
  (let [elaborated-bar (if (seq bar-melody)
                         (elaborate-4th state current-working-bar)
                         current-working-bar)
        new-bar (if (seq rest-working-bars)
                  (elaborate-suspension-with-next-working-bar
                   elaborated-bar (first rest-working-bars))
                  elaborated-bar)
        ;; _ (println "NEW BAR" new-bar)
        new-bar-melody (conj bar-melody new-bar)
        new-state (-> state
                      (assoc :bar-melody new-bar-melody)
                      (assoc :previous-melody (last (:n (last new-bar))))
                      (assoc :cantus-note (first rest-cantus))
                      (assoc :rest-cantus (rest rest-cantus)))]
    ;; (println "NEW STATE" new-state)
    (if (seq rest-working-bars)
      (elaborate-bar-melody-iter
       (first rest-working-bars) (rest rest-working-bars)
       new-state)
      (:bar-melody new-state))))

(defn- elaborate-4th-species [species key-sig]
  (let [cantus (get-cantus species)
        rev-cantus (reverse cantus)
        position (get-position species)
        working-bar-melody (reverse-bar-melody (get-counter species))
        elab-bars (reverse-bar-melody
                   (elaborate-bar-melody-iter
                    (first working-bar-melody)
                    (rest working-bar-melody)
                    {:key key-sig
                     :position position
                     :bar-melody []
                     :previous-melody nil
                     :cantus-note (first rev-cantus)
                     :rest-cantus (rest rev-cantus)}))]
    (make-fifth-species cantus elab-bars position)))

(def generate-fifth
  (generate-template
   candidates
   evaluate-fifth-species
   make-fifth-species
   fifth-species-rules?
   next-node-fifth
   elaborate-4th-species))

(defn play-best-fifth [n cf position]
  (generate-fifth n cf position
                  {:pattern ""
                   :midi "acoustic grand"})
  (sh/sh "timidity" "resources/temp.midi"))

