(ns counterpoint.second-species-examples 
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.cantus-firmi-examples :refer [fux-a fux-c fux-e
                                                        fux-f haydn haydn-a
                                                        mozart-c1 mozart-c2 salieri]]
            [counterpoint.lilypond :refer [second-species->lily]]
            [counterpoint.melody :refer [make-melody melody-range]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [make-second-species
                                                 second-species-rules?]]
            [counterpoint.utils :refer [rule-warning]]))

(comment



  (def species (let [counterpoint-melody
                     (make-melody n/c4 n/b4 n/a4 n/b4
                                  n/c4 n/g4 n/e4 n/c4
                                  n/b4 n/d4 n/a4 n/b4
                                  n/c4 n/g3 n/a4 n/b4
                                  n/c4)]
                 (make-second-species salieri counterpoint-melody :above)))
  (second-species->lily species)

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
  (second-species-rules? species)
  (second-species->lily species)

  (sh/sh "timidity" "resources/temp.midi")
  
  (count fux-a)
;
  )