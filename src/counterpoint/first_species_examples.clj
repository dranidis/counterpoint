(ns counterpoint.first-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-a fux-d haydn-a
                                                        salieri]]
            [counterpoint.first-species :refer [evaluate-species
                                                first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [generate-reverse-random-counterpoint]]
            [counterpoint.lilypond :refer [first-species->lily]]
            [counterpoint.melody :refer [make-melody melodic-intervals
                                         transpose]]
            [counterpoint.notes :as n]))

(def fux-d-cp-above (make-melody n/a4 n/a4 n/g3 n/a4 n/b4 n/c4 n/c4 n/b4 n/d4 n/c#4 n/d4))
(def fux-d-cp-below (make-melody n/d2 n/d2 n/a3 n/f2 n/e2 n/d2 n/f2 n/c3 n/d3 n/c#3 n/d3))

(def fux-d-above (make-first-species fux-d-cp-above fux-d :above))
(def fux-d-below (make-first-species fux-d-cp-below fux-d :below))

(def fux-a-cp-below (make-melody n/a3 n/e2 n/g2 n/f2
                                 n/a3 n/g2 n/f2 n/g2
                                 n/f2 n/e2 n/g#2 n/a3))
(def fux-a-below (make-first-species fux-a-cp-below fux-a :below))
(def fux-a-above (make-first-species (transpose fux-a-cp-below 1) fux-a :above))
(first-species->lily fux-a-above "treble")
(first-species->lily fux-a-below "treble_8")
(first-species->lily fux-a-below "treble_8")
(sh/sh "timidity" "resources/temp.midi")

(evaluate-species fux-a-above)
(evaluate-species fux-a-below)

(evaluate-species fux-d-above)
(evaluate-species fux-d-below)


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

  (def species (make-first-species
                haydn-a
                (generate-reverse-random-counterpoint :above :c haydn-a) :above))


  (def position :below)
  (defn generate-species-eval [cf]
    (let [cp (generate-reverse-random-counterpoint :below :c cf)
          species (make-first-species cf cp position)
          score (evaluate-species species)]
      [species score]))

  (def species100 (map (fn [_] (generate-species-eval fux-a)) (range 1000)))

  (def unique-sorted-species100 (sort #(> (second %1) (second %2))
                                      (into [] (into #{} species100))))
  (def n 0)
  (count unique-sorted-species100)
  (first-species->lily (first (nth unique-sorted-species100 n))
                       (if (= position :above) "treble" "treble_8"))
  (println (evaluate-species (first (nth unique-sorted-species100 n))))
  (sh/sh "timidity" "resources/temp.midi")




  (let [cf salieri
        position :above
        cp (generate-reverse-random-counterpoint position :c cf)
        species (make-first-species cf cp position)
        ;; _ (println cp)
        _ (println "RULES " (first-species-rules? species))
        _ (println "EVAL  " (evaluate-species species))]
    (first-species->lily species
                         (if (= position :above) "treble" "treble_8")))

  (sh/sh "timidity" "resources/temp.midi")
  ;; (sh/sh "timidity" "resources/temp.mid")

  ;
  )
