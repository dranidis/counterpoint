(ns counterpoint.first-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.first-species :refer [first-species-rules?]]
            [counterpoint.first-species-type :refer [get-counter
                                                     make-first-species]]
            [counterpoint.generate-first-species :refer [generate-random-counterpoint-above
                                                         generate-reverse-random-counterpoint-above]]
            [counterpoint.lilypond :refer [first-species->lily]]
            [counterpoint.melody :refer [make-melody melodic-intervals]]
            [counterpoint.notes :as n]))

(comment
  (def species (let [counterpoint-melody
                     (make-melody n/d3 n/d3 n/a4 n/f3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
                     cantus-firmus
                     (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
                 (make-first-species cantus-firmus counterpoint-melody :below)))

  (def species (let [counterpoint-melody
                     (make-melody n/a4 n/a4 n/g3 n/a4 n/b4 n/c4 n/c4 n/b4 n/d4 n/c#4 n/d4)
                     cantus-firmus
                     (make-melody n/d3 n/f3 n/e3 n/d3 n/g3 n/f3 n/a4 n/g3 n/f3 n/e3 n/d3)]
                 (make-first-species cantus-firmus counterpoint-melody :above)))

  (def species
    (let [counterpoint-melody
          (make-melody n/e4 n/f4 n/g4 n/a5 n/e4 n/f4 n/f4 n/d4 n/e4)
          _ (print (melodic-intervals counterpoint-melody))
          cantus-firmus
          (make-melody n/e3 n/d3 n/e3 n/f3 n/g3 n/a4 n/d3 n/f3 n/e3)]
      (make-first-species cantus-firmus counterpoint-melody :above)))



  (def species (let [counterpoint-melody
                     (make-melody n/d4 n/c4 n/d4 n/f4 n/e4 n/a5 n/g4 n/e4 n/d4 n/c#4 n/d4)
                     cantus-firmus ;; haydn
                     (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3)]
                 (make-first-species cantus-firmus counterpoint-melody :above)))

  (def species (let [counterpoint-melody
                     (make-melody n/c4 n/a4 n/b4 n/c4 n/e4 n/d4 n/g3 n/b4 n/c4)
                     cantus-firmus ;; salieri
                     (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3)]
                 (make-first-species cantus-firmus counterpoint-melody :above)))


  (def fux-d (make-melody n/d3 n/f3 n/e3 n/d3 n/g3 n/f3 n/a4 n/g3 n/f3 n/e3 n/d3))
  (def fux-e (make-melody n/e3 n/c3 n/d3 n/c3 n/a3 n/a4 n/g3 n/e3 n/f3 n/e3))
  (def fux-g (make-melody n/g2 n/c3 n/b3 n/g2 n/c3 n/e3 n/d3 n/g3 n/e3 n/c3 n/d3 n/b3 n/a3 n/g2))
  (def fux-a (make-melody n/a3 n/c3 n/b3 n/d3 n/c3 n/e3 n/f3 n/e3 n/d3 n/c3 n/b3 n/a3))

  (def haydn (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3))
  (def salieri (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3))

  (generate-random-counterpoint-above :c haydn)
  (make-first-species haydn (generate-random-counterpoint-above :c haydn) :above)
  (def species (make-first-species haydn (generate-random-counterpoint-above :c haydn) :above))

  (generate-reverse-random-counterpoint-above :c haydn)


  (first-species->lily (make-first-species
                        haydn
                        (generate-random-counterpoint-above :c haydn) :above))

  (def species (make-first-species
                fux-a
                (generate-reverse-random-counterpoint-above :c fux-a) :above))
  (first-species-rules? species)
  (first-species->lily species)
  (sh/sh "timidity" "resources/temp.midi")


  (print (first-species->lily species))

  ;
  )