(ns counterpoint.gen-second-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-d salieri-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.first-species-type :refer [get-counter]]
            [counterpoint.gen-first-dfs :refer [dfs-solution->cp
                                                last-note-candidates second-to-last-note
                                                solution?]]
            [counterpoint.generate-second-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval m2 m2- M3- m6 m6-
                                            note-at-melodic-interval P12- P5 P5-]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [melody-score]]
            [counterpoint.second-species :refer [evaluate-second-species
                                                 make-second-species
                                                 second-species-rules?]]))

(defn- third-to-last-note [position previous-melody previous-cantus cantus-note]
  (let [intval (if (= position :above)
                 (if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
                   (if (= m2 (interval cantus-note previous-cantus))
                     m6
                     P5)
                   (if (= m2 (interval cantus-note previous-cantus))
                     m6
                     P5- ;; crossing 
                     ))
                 (if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
                   (if (= m2 (interval cantus-note previous-cantus))
                     M3-
                     P12-)
                   (if (= m2 (interval cantus-note previous-cantus))
                     m6 ;; crossing
                     (if (= m2- (interval cantus-note previous-cantus))
                       m6- ;; fa-mi
                       P5-))))]
    (note-at-melodic-interval cantus-note intval)))

(defn candidates [[position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   cantus-note
                   cantus-notes]]
  (case (count melody)
    0 (map (fn [n] [n]) (last-note-candidates position cantus-note))
    1 [[(second-to-last-note position previous-melody previous-cantus cantus-note)
        (third-to-last-note position previous-melody previous-cantus cantus-note)]]
    (next-reverse-candidates
     position key melody m36s previous-melody previous-cantus cantus-note)))

(defn next-node [[position
                  key
                  melody
                  m36s ;; counter of thirds & sixths
                  previous-melody
                  previous-cantus
                  cantus-note
                  cantus-notes]
                 current]
  ;; (println melody current)
  (let [prev-melody (if (= 1 (count current))
                      (first current)
                      (last current))]
    [position
     key
     (into melody current)
     (update-m36-size m36s position cantus-note current)
     prev-melody
     cantus-note
     (first cantus-notes)
     (rest cantus-notes)]))

(defn generate-reverse-counterpoint-2nd-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
        melody []
        previous-melody nil
        previous-cantus nil
        root-node [position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   (first rev-cantus)
                   (rest rev-cantus)]]
    (generate-dfs-solutions root-node candidates next-node solution?)))

(defn play-best-second [cf key position]
  (let [cps (generate-reverse-counterpoint-2nd-dfs position key cf)
        _ (println "ALL" (count cps))
        species (apply max-key #(let [e (evaluate-second-species  %)]
                                ;; (println e)
                                  e)
                       (map #(make-second-species cf (dfs-solution->cp %) position) cps))
        _ (println "RULES " (second-species-rules? species))
        _ (println "EVAL  " (evaluate-second-species species))]
    (species->lily species
                   {:clef
                    (if (= position :above)
                      "treble"
                      "treble_8")
                    :pattern ""
                    :tempo "4 = 240"})
    (sh/sh "timidity" "resources/temp.midi")
  ;
    ))

;; (play-best-second fux-d :c :above)

(comment
  (def position :above)
  (def cf fux-d)
  (def at-key :c)
  (def cps (generate-reverse-counterpoint-2nd-dfs position at-key cf))

  (def first-sol (make-second-species cf (dfs-solution->cp (second cps)) position))

  ;; (def best-sol (apply max-key #(let [e (evaluate-second-species  %)]
  ;;                             ;;  (println e)
  ;;                                 e)
  ;;                      (map #(make-second-species cf (dfs-solution->cp %) position) cps)))


  ;; (second-species-rules? first-sol)
  ;; (println "RULES " (second-species-rules? best-sol))
  (evaluate-second-species first-sol)
  (species->lily first-sol
                 {:clef (if (= position :above)
                          "treble"
                          "treble_8")
                    ;; :pattern "baaa"
                  :pattern ""
                  :tempo "4 = 200"})
  (sh/sh "timidity" "resources/temp.midi")

  (def cp (get-counter first-sol))
  (melody-score cp)

  (map-indexed vector cp)
  (filter #(not= % :rest) cp)
  ;
  )