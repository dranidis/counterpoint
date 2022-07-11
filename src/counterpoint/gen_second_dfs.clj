(ns counterpoint.gen-second-dfs
  (:require [clojure.java.shell :as sh]
            [clojure.pprint :as pprint]
            [counterpoint.cantus-firmi-examples :refer [fux-d haydn-a]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.gen-first-dfs :refer [dfs-solution->cp
                                                last-note-candidates second-to-last-note
                                                solution?]]
            [counterpoint.generate-second-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval
                                            note-at-melodic-interval P12- P5 P5-]]
            [counterpoint.lilypond :refer [melody->lily species->lily]]
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


;; (def cps (generate-reverse-counterpoint-2nd-dfs :below :c fux-d))
;; (def cp (dfs-solution->cp (first cps)))
;; (def species (make-second-species fux-d cp :below))
;; species
;; (pprint/pp)
;; (species->lily species)
;; (second-species-rules? species)
;; (sh/sh "timidity" "resources/temp.midi")

(comment
  (def position :below)
  (def cf haydn-a)
  (def at-key :c)
  (def cps (generate-reverse-counterpoint-2nd-dfs position at-key cf))

;; (println (if (= 0 (count cps)) "NO SOLUTIONS" (str (count cps) " solutions")))
;; (def cp (reverse (nth (first cps) 2)))

  (def species (make-second-species cf (dfs-solution->cp (first cps)) position))
  cps
  species
;; (def species (apply max-key #(let [e (evaluate-second-species  %)]
;;                               ;;  (println e)
;;                                e)
;;                     (map #(make-second-species cf (reverse (nth % 2)) position) cps)))

;; (map #(evaluate-second-species (make-second-species cf (reverse (nth % 2)) position)) cps)

  (println "RULES " (second-species-rules? species))
  (evaluate-second-species species)
  (species->lily species
                 {:clef (if (= position :above)
                          "treble"
                          "treble_8")
                    ;; :pattern "baaa"
                  :pattern ""
                  :tempo "4 = 200"})
;; (sh/sh "timidity" "resources/temp.midi")

  (melody->lily haydn-a))