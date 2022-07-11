(ns counterpoint.gen-second-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [salieri-c salieri-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.gen-first-dfs :refer [dfs-solution->cp
                                                last-note-candidates second-to-last-note
                                                solution?]]
            [counterpoint.generate-second-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval
                                            note-at-melodic-interval P12- P5 P5-]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.second-species :refer [evaluate-second-species
                                                 make-second-species
                                                 second-species-rules?]]))

(defn- third-to-last-note [position previous-melody previous-cantus cantus-note]
  (if (= position :above)
    (note-at-melodic-interval cantus-note P5)
    (if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
      (note-at-melodic-interval cantus-note P12-)
      (note-at-melodic-interval cantus-note P5-))))

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
  [position
   key
   (into melody current)
   (update-m36-size m36s position cantus-note current)
   (if (= 0 (count melody))
     (first current)
     (second current))
   cantus-note
   (first cantus-notes)
   (rest cantus-notes)])

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

;; (play-best-second salieri-d :key :above)

(comment
  (def position :below)
  (def cf salieri-d)
  (def at-key :c)
  (def cps (generate-reverse-counterpoint-2nd-dfs position at-key cf))

  (def first-sol (make-second-species cf (dfs-solution->cp (first cps)) position))

  (def best-sol (apply max-key #(let [e (evaluate-second-species  %)]
                              ;;  (println e)
                                  e)
                       (map #(make-second-species cf (dfs-solution->cp %) position) cps)))

  
  (second-species-rules? best-sol)
  (println "RULES " (second-species-rules? best-sol))
  (evaluate-second-species best-sol)
  (species->lily best-sol
                 {:clef (if (= position :above)
                          "treble"
                          "treble_8")
                    ;; :pattern "baaa"
                  :pattern ""
                  :tempo "4 = 200"})
  (sh/sh "timidity" "resources/temp.midi")
  ;
  )