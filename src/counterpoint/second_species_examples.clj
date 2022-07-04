(ns counterpoint.second-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.cantus-firmi-examples :refer [fux-a fux-c fux-d
                                                        fux-e fux-f haydn haydn-a
                                                        mozart-c1 mozart-c2 salieri-c]]
            [counterpoint.figured-bass :refer [figured-bass-second]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [make-melody melodic-intervals
                                         melody-range]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as rest]
            [counterpoint.second-species :refer [correct-downbeat-intervals
                                                 correct-upbeat-intervals
                                                 get-low-high-second make-second-species
                                                 no-undisguised-direct-motion-of-downbeats-to-perfect? second-species-rules?]]
            [counterpoint.utils :refer [rule-warning]]))

(comment
  (def species (let [counterpoint-melody
                     (make-melody n/c4 n/b4 n/a4 n/b4
                                  n/c4 n/g4 n/e4 n/c4
                                  n/b4 n/d4 n/a4 n/b4
                                  n/c4 n/g3 n/a4 n/b4
                                  n/c4)]
                 (make-second-species salieri-c counterpoint-melody :above)))

  (species->lily species {:pattern "brba"} )
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

  (species->lily species {:pattern "abba"})
(sh/sh "timidity" "resources/temp.mid")

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
                      (make-second-species fux-d counterpoint-melody :above)))
  (second-species-rules? mozart-fux-d)
  (species->lily mozart-fux-d)

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
  
  (melodic-intervals rest-melody)

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
                      (make-second-species fux-d counterpoint-melody :above)))
  
  (figured-bass-second salzer-fux-d)
  (partition 2 (first (get-low-high-second salzer-fux-d)))
(partition 2 (second (get-low-high-second salzer-fux-d)))

  (allowed-melodic-intervals? salzer-fux-d)
  (correct-downbeat-intervals salzer-fux-d)
  (correct-upbeat-intervals salzer-fux-d)
  (no-undisguised-direct-motion-of-downbeats-to-perfect? salzer-fux-d)

  (second-species-rules? salzer-fux-d)
  (species->lily salzer-fux-d)

  (second-species-rules? species)
  (species->lily species)

  (sh/sh "timidity" "resources/temp.midi")
  (sh/sh "timidity" "resources/temp.mid")
  

  (count fux-a)
  (partition 2 [1 2 3])
;
  )