(ns counterpoint.second-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [get-melody maximum-range-M10?]]
            [counterpoint.cantus-firmi-examples :refer [fux-a fux-c fux-d
                                                        fux-e fux-f fux-g haydn-a
                                                        mozart-c1 mozart-c2 salieri-c]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [make-melody melodic-intervals-no-rest
                                         melody-range]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as rest]
            [counterpoint.second-species :refer [correct-downbeat-intervals
                                                 correct-upbeat-intervals
                                                 evaluate-second-species make-second-species
                                                 no-undisguised-direct-motion-of-downbeats-to-perfect? second-species-rules?]]
            [counterpoint.species-type :refer [get-low-high]]
            [counterpoint.utils :refer [rule-warning]]))

;; Unsolvable
;; below haydn-a, cf-c, cf-a, fux-g

(comment
  (def species (let [counterpoint-melody
                     (make-melody n/c4 n/b4 n/a4 n/b4
                                  n/c4 n/g4 n/e4 n/c4
                                  n/b4 n/d4 n/a4 n/b4
                                  n/c4 n/g3 n/a4 n/b4
                                  n/c4)]
                 (make-second-species salieri-c counterpoint-melody :above)))

  (species->lily species {:pattern "brba"})
  (sh/sh "timidity" "resources/temp.midi")

  (def species (let [counterpoint-melody
                     (make-melody n/d4 n/a4
                                  n/b4 n/c4
                                  n/d4 n/e4
                                  n/f4 n/d4
                                  n/c4 n/e4
                                  n/f4 n/d4
                                  n/c4 n/g3
                                  n/b4 n/c4
                                  n/d4 n/a4
                                  n/b4 n/c#4
                                  n/d4)]
                 (make-second-species haydn counterpoint-melody :above)))

  (species->lily species {:pattern "abra"})
  (sh/sh "timidity" "resources/temp.midi")

  (def species (let [counterpoint-melody
                     (make-melody n/a4 n/e3
                                  n/a4 n/e4
                                  n/d4 n/g3
                                  n/g4 n/f4
                                  n/e4 n/d4
                                  n/c4 n/a4
                                  n/d4 n/b4
                                  n/c4 n/d4
                                  n/e4 n/e3
                                  n/f#3 n/g#3
                                  n/a4)]
                 (make-second-species haydn-a counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/b4 n/c4
                                  n/a4 n/c4
                                  n/f4 n/f3
                                  n/a4 n/b4
                                  n/c4 n/a4
                                  n/c4 n/d4
                                  n/e4 n/b4
                                  n/e4 n/b4
                                  n/c4 n/d4
                                  n/e4)]
                 (make-second-species fux-e counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/f4 n/c4
                                  n/d4 n/e4
                                  n/f4 n/g4
                                  n/a5 n/g4
                                  n/f4 n/a5
                                  n/c5 n/bb5
                                  n/a5 n/f4
                                  n/e4 n/d4
                                  n/c4 n/e4
                                  n/f4 n/e4
                                  n/d4 n/e4
                                  n/f4)]
                 (make-second-species fux-f counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/g3 n/a4
                                  n/b4 n/d4
                                  ;; n/c4 n/d4 ;; disguised directed 5th
                                  n/c4 n/g4 ;; avoid disguised directed 5th
                                  n/e4 n/f4
                                  n/g4 n/f4
                                  n/e4 n/d4
                                  ;; n/c4 n/b4 ;; disguised directed 5ths
                                  n/c4 n/a4 ;; disguised directed 5ths
                                  n/b4 n/a4
                                  n/g3 n/f3
                                  ;; n/e3 n/g3 ;; disguised directed 5ths
                                  n/e3 n/c4 ;; avoid disguised directed 5ths
                                  n/a4 n/b4
                                  n/c4)]
                 (make-second-species mozart-c1 counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/c4 n/b4
                                  n/a4 n/b4
                                  n/c4 n/g4
                                  n/f4 n/e4
                                  n/d4 n/e4
                                  n/f4 n/c4
                                  n/e4 n/d4
                                  n/c4 n/e4
                                  n/d4 n/a4
                                  n/b4 n/d4
                                  n/c4 n/a4
                                  n/c4 n/b4
                                  n/c4 n/a4
                                  n/g3 n/c4
                                  n/a4 n/b4
                                  n/c4)]
                 (make-second-species mozart-c2 counterpoint-melody :above)))
  (def species (let [counterpoint-melody
                     (make-melody n/e4 n/d4
                                  n/c4 n/e4
                                  n/d4 n/c4
                                  n/b4 n/a4
                                  n/g3 n/b4
                                  n/c4 n/d4
                                  n/e4 n/f4
                                  n/g4 n/e4
                                  n/c4 n/a4
                                  n/g3 n/c4
                                  n/a4 n/b4
                                  n/c4)]
                 (make-second-species fux-c counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/e4 n/d4
                                  n/c4 n/a4
                                  n/d4 n/e4
                                  n/f4 n/d4
                                  n/g4 n/f4
                                  n/e4 n/d4
                                  n/c4 n/a4
                                  n/c4 n/g3
                                  n/b4 n/d4
                                  n/e4 n/e3
                                  n/f#3 n/g#3
                                  n/a4)
                     _ (rule-warning (maximum-range-M10? counterpoint-melody) #(str "Maximum range violation! " (melody-range counterpoint-melody)))]
                 (make-second-species fux-a counterpoint-melody :above)))

  (def mozart-fux-d (let [counterpoint-melody
                          (make-melody n/a4 n/d4
                                       n/a4 n/b4
                                       n/c4 n/g3
                                       n/f3 n/a4
                                       n/bb4 n/g3
                                       n/a4 n/c4
                                       n/f4 n/c4
                                       n/b4 n/e4
                                       n/d4 n/a4
                                       n/b4 n/c#4
                                       n/d4)]
                      (make-second-species (get-melody fux-d) counterpoint-melody :above)))
  (second-species-rules? mozart-fux-d)
  (evaluate-second-species mozart-fux-d :verbose true)
  (species->lily mozart-fux-d)

  (let [[low high ] (get-low-high mozart-fux-d)]
    (println "H" high)
    (println "L" low)
    (println "HP" (map first (partition 2 high)))
    (println "LP" (map first (partition 2 low))))


  (def rest-melody (make-melody rest/r
                          ;;  n/a4 ;; first a4 should be a rest when implemented!!
                                n/d4
                                n/a4 n/b4
                                n/c4 n/g3
                                n/f3 n/a4
                                n/bb4 n/g3
                                n/a4 n/f4
                                n/e4 n/f4
                                n/g4 n/e4
                                n/a5 n/a4
                                n/b4 n/c#4
                                n/d4))

  (melodic-intervals-no-rest rest-melody)

  (def salzer-fux-d (let [counterpoint-melody
                          (make-melody rest/r
                          ;;  n/a4 ;; first a4 should be a rest when implemented!!
                                       n/d4
                                       n/a4 n/b4
                                       n/c4 n/g3
                                       n/f3 n/a4
                                       n/bb4 n/g3
                                       n/a4 n/f4
                                       n/e4 n/f4
                                       n/g4 n/e4
                                       n/a5 n/a4
                                       n/b4 n/c#4
                                       n/d4)]
                      (make-second-species (get-melody fux-d) counterpoint-melody :above)))
  
  (evaluate-second-species salzer-fux-d :verbose true)
  (species->lily salzer-fux-d)

  (def fux-g-2nd (let [counterpoint-melody
                       (make-melody rest/r
                                    n/g2
                                    n/e2 n/f2
                                    n/g2 n/f2
                                    n/e2 n/d2
                                    n/c2 n/e2
                                    n/c2 n/c3
                                    n/b3 n/a3
                                    n/g2 n/b3

                                    n/c3 n/b3
                                    n/a3 n/g2
                                    n/f#2 n/d2
                                    n/g2 n/b2
                                    n/d2 n/f#2 n/g2)]
                   (make-second-species fux-g counterpoint-melody :below)))

  (allowed-melodic-intervals? salzer-fux-d)
  (correct-downbeat-intervals salzer-fux-d)
  (correct-upbeat-intervals salzer-fux-d)
  (no-undisguised-direct-motion-of-downbeats-to-perfect? salzer-fux-d)

  (second-species-rules? salzer-fux-d)
  (species->lily salzer-fux-d)

  (second-species-rules? fux-g-2nd)
  (species->lily fux-g-2nd {:clef      "treble_8"
                            :pattern ""
                            :tempo "4 = 200"})

  (sh/sh "timidity" "resources/temp.midi")
  (sh/sh "timidity" "resources/temp.mid")

  (def test-sp (let [counterpoint-melody
                       (make-melody n/d3 n/c3
                                    n/bb3 n/f2
                                    n/a3 n/c#3
                                    n/d3)
                  cf (make-melody n/d3 
                                  n/f3 
                                  n/e3 
                                  n/d3)]
                   (make-second-species cf counterpoint-melody :below)))
  
  (species->lily test-sp)
(second-species-rules? test-sp)
;
  )
