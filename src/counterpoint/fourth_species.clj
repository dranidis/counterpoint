(ns counterpoint.fourth-species
  (:require [counterpoint.cantus-firmi-examples :refer [bellerman-a fux-d
                                                        schenker-d schoenberg-c]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position
                                                     make-first-species]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?]]
            [counterpoint.lilypond :refer [fourth-species->lily]]
            [counterpoint.melody :refer [double-melody insert-to-melody
                                         make-melody transpose]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as rest]
            [counterpoint.utils :refer [rule-warning]]))

(defn make-fourth-species [cantus-firmus counterpoint-melody arg3]
  (make-first-species cantus-firmus counterpoint-melody arg3))

(defn get-low-high-fourth [species]
  (let [counter (insert-to-melody rest/r (get-counter species))
        cantus (get-cantus species)
        double-cantus (double-melody cantus)]
    (if (= (get-position species) :above)
      [double-cantus counter]
      [counter double-cantus])))

(defn consonant-upbeats? [species]
  (let [[low high] (get-low-high-fourth species)]
    (every? harmonic-consonant?
            (map second (partition 2 (map simple-interval low high))))))

(defn consonant-or-resolving-downbeats? [species]
  (let [[low high] (get-low-high-fourth species)
        downbeat-intervals (rest (map first (partition 2 (map simple-interval low high))))
        melody-pairs (rest (partition 2 (if (= (get-position species) :above) high low)))
        melody-intervals (map simple-interval
                              (map first melody-pairs)
                              (map second melody-pairs))]
    (every? identity (map (fn [x y]
                            (or (harmonic-consonant? x)
                                (= -2 (get-interval y)))) downbeat-intervals melody-intervals))))


;; upbeats are always consonant
;; Dissonant ligatures must resolve downward by step to a consonance
;; allowed suspensions: 
;;  upper voice: 4-3, 7-6, 9-8 (not often 2-1)
;;  lower voice: 2-3, 4-5, 9-10 (no 7-8)
;; Ligatures cannot be used to disguise voice leading errors 
;; After a consonant ligaure usually a leap to a consonant downbeat

;; two perfect consonances of the same interval must not be separated
;; only by a dissonant suspension.

;; ending: upper 7-6 suspension, lower 2-3

(defn fourth-species-rules? [species]
  (and
   (rule-warning (= (- (* 2 (count (get-cantus species))) 2)
                    (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
   (allowed-melodic-intervals? species)
   (rule-warning (consonant-upbeats? species)
                 #(println "Upbeats are not consonant!"))
   (rule-warning (consonant-or-resolving-downbeats? species) 
                 #(println "Check downbeats! Must be consonant or resolving"))
   ))



(def fux-d-4-species (make-fourth-species
                      fux-d
                      (make-melody n/a4 n/a4
                                   n/d4 n/d4 n/c4 n/c4 n/bb4 n/bb4
                                   n/g3
                                   n/a4
                                   n/c4 n/c4
                                   n/f4 n/f4 n/e4 n/e4 n/d4 n/d4 n/c#4
                                   n/d4)
                      :above))
(fourth-species->lily fux-d-4-species "treble")
(fourth-species-rules? fux-d-4-species)
;; (sh/sh "timidity" "resources/temp.midi")

(count bellerman-a)
(def bellerman-a-4-species (make-fourth-species
                            bellerman-a
                            (make-melody n/c4 n/c4
                                         n/b4 n/b4 n/c4 n/d4 n/g4 n/g4
                                         n/f4
                                         n/f4
                                         n/e4 n/e4
                                         n/d4 n/a5 n/e4 n/f#4
                                         n/g#4
                                         n/a5)
                            :above))
(fourth-species-rules? bellerman-a-4-species)


(def bellerman-a-4-species-below (make-fourth-species
                                  bellerman-a
                                  (transpose (make-melody
                                              n/a4 n/b4 n/g3
                                              n/a4
                                              n/c4
                                              n/c4 n/b4
                                              n/b4

                                              n/b5  n/b5 n/a5

                                            ;;  n/g3  n/a4 n/c4 

                                              n/d4 n/e4
                                              n/e4 n/a5
                                              n/a5 n/g#4
                                              n/a5) -1)
                                  :below))
(fourth-species->lily bellerman-a-4-species-below "treble")
(fourth-species-rules? bellerman-a-4-species-below)
;; (sh/sh "timidity" "resources/temp.midi")

(def schenker-d-4-species (make-fourth-species
                           schenker-d
                           (make-melody n/d4 
                                        n/d4 n/c4 
                                        n/c4 n/a5 

                                        n/a5 n/g4 
                                        n/g4 n/f4 
                                        n/f4 n/e4 
                                        n/e4 n/b4 

                                        n/c4 n/d4 
                                        n/d4 n/c#4
                                        n/d4)
                           :above))
(fourth-species->lily schenker-d-4-species "treble")
(fourth-species-rules? schenker-d-4-species)
(sh/sh "timidity" "resources/temp.midi")

(def schoenberg-c-4-species (make-fourth-species
                             schoenberg-c
                             (make-melody n/g3
                                          n/g3 n/d3
                                          n/e3 n/d3
                                          n/d3 n/c3
                                          n/c3 n/e3
                                          n/e3 n/d3
                                          n/d3 n/c3
                                          n/c3 n/b3
                                          n/b3 n/d3
                                          n/d3 n/c3
                                          n/c3 n/b3
                                          n/c3)
                             :above))
;; (fourth-species->lily schoenberg-c-4-species "treble_8")

(fourth-species-rules? schoenberg-c-4-species)


;; (sh/sh "timidity" "resources/temp.midi")


