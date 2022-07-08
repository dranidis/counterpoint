(ns counterpoint.gen-second-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fetis-c]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.gen-first-dfs :refer [solution?]]
            [counterpoint.generate-second-species :refer [ending-second
                                                          next-reverse-candidates
                                                          update-m36-size]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.second-species :refer [make-second-species
                                                 second-species-rules?]]))


(defn candidates [[position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   cantus-note
                   cantus-notes]]
  (next-reverse-candidates
   position key melody m36s previous-melody previous-cantus cantus-note))

(defn next-node [[position
                  key
                  melody
                  m36s ;; counter of thirds & sixths
                  previous-melody
                  previous-cantus
                  cantus-note
                  cantus-notes]
                 current]
;;   (if (empty? cantus-notes)
;;       (into melody current)
  
  [position
   key
   (into melody current)
   (update-m36-size m36s position cantus-note current)
   (second current)
   cantus-note
   (first cantus-notes)
   (rest cantus-notes)])

(defn generate-reverse-random-counterpoint-2nd-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        [last-melody second-last-melody third-last-melody m36s]
        (ending-second position rev-cantus)
        melody [last-melody second-last-melody third-last-melody]
        root-node [position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   third-last-melody
                   (second rev-cantus)
                   (nth rev-cantus 2)
                   (subvec (into [] rev-cantus) 3)]]
    (generate-dfs-solutions root-node candidates next-node solution?)))

(def position :above)
(def cf fetis-c)
(def at-key :c)
(def cps (generate-reverse-random-counterpoint-2nd-dfs position at-key cf))
(println (if (= 0 (count cps)) "NO SOLUTIONS" (str (count cps) "solutions")))
(def cp (reverse (nth (first cps) 2)))
(def species (make-second-species cf cp position))

(println "RULES " (second-species-rules? species))

(species->lily species
               {:clef (if (= position :above)
                        "treble"
                        "treble_8")
                    ;; :pattern "baaa"
                :pattern ""
                :tempo "4 = 200"})
(sh/sh "timidity" "resources/temp.midi")