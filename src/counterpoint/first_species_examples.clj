(ns counterpoint.first-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [albrechtsberger-d
                                                        albrechtsberger-f
                                                        boulanger-e boulanger-g cf-a cf-c fetis-c fux-a fux-d haydn-a salieri-c salieri-d]]
            [counterpoint.first-species :refer [evaluate-species
                                                first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [generate-reverse-random-counterpoint]]
            [counterpoint.lilypond :refer [first-species->lily melody->lily]]
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
;; (sh/sh "timidity" "resources/temp.midi")

(evaluate-species fux-a-above)
(evaluate-species fux-a-below)

(evaluate-species fux-d-above)
(evaluate-species fux-d-below)

(def shubert-first-species-above-salieri-c (make-first-species salieri-c
                                                               (make-melody
                                                                n/e4 n/d4 n/c4 n/c4 n/b4 n/d4 n/g3 n/b4 n/c4)
                                                               :above))
(def shubert-first-species-below-salieri-c (make-first-species salieri-c
                                                               (make-melody
                                                                n/c3 n/d3 n/c3 n/c3 n/e3 n/a3 n/a3 n/b3 n/c3)
                                                               :below))
(def shubert-first-species-above-salieri-d
  (make-first-species salieri-d
                      (make-melody
                       n/d4
                       n/c#4
                    ;;   n/c4 
                       n/d4 n/f4
                       n/c#4 n/c#4
                    ;;    n/c4 n/c4 ;; avoiding the aug 4
                       n/d4 n/f4 n/e4 n/d4)
                      :above))

(def shubert-first-species-below-salieri-d
  (make-first-species (transpose salieri-d 1)
                      (transpose (make-melody
                                  n/d4
                                  n/c#4
                    ;;   n/c4 
                                  n/d4 n/f4
                                  n/d4 ;;   n/c#4 
                                  n/c#4
                    ;;    n/c4 n/c4 ;; avoiding the aug 4
                                  n/d4 n/f4 n/e4 n/d4) -1)
                      :below))

(first-species->lily shubert-first-species-above-salieri-c "treble")
(first-species->lily shubert-first-species-below-salieri-c "treble")
(first-species->lily shubert-first-species-above-salieri-d "treble")
(first-species->lily shubert-first-species-below-salieri-d "treble")


(evaluate-species shubert-first-species-above-salieri-c)
(evaluate-species shubert-first-species-above-salieri-d)
(evaluate-species shubert-first-species-below-salieri-c)

(def fetis-first-species (make-first-species
                          fetis-c
                          (make-melody n/g3 n/f3 n/g3
                                       n/a4 n/b4 n/d4 n/c4
                                       n/c4 n/d4 n/e4
                                       n/d4 n/a4 n/c4
                                       n/b4 n/c4)
                          :above))
(first-species->lily fetis-first-species "treble")
;; (sh/sh "timidity" "resources/temp.midi")


(melody->lily fetis-c)
(melody->lily cf-c)
(melody->lily cf-a)
(melody->lily albrechtsberger-f)
(melody->lily albrechtsberger-d)
(melody->lily boulanger-g)
(melody->lily boulanger-e)

(def boulanger-g-ex-above
  (make-first-species boulanger-g
                      (make-melody
                       n/d4 n/e4 n/d4
                       n/g4 n/f#4 n/d4
                       n/c4 n/a4 n/d3
                       n/f#3 n/g3)
                      :above))
(first-species->lily boulanger-g-ex-above "treble")

(def boulanger-e-ex-above
  (make-first-species boulanger-e
                      (make-melody
                       n/e4 n/e4 n/g4
                       n/c4 n/d4 n/d4
                       n/c4 n/b4
                       n/d#4 n/e4)
                      :above))
(first-species->lily boulanger-e-ex-above "treble")
(first-species-rules? boulanger-e-ex-above)
(evaluate-species boulanger-e-ex-above)
;; (sh/sh "timidity" "resources/temp.midi")

(def exercise-cf-c
  (make-first-species cf-c

                      (make-melody
                       n/c4 n/b4  n/d4
                       n/e4
                       n/a4 n/b4 n/a4 n/c4
                       n/b4 n/c4)
                      :above))
(first-species->lily exercise-cf-c "treble")
(evaluate-species exercise-cf-c)

(def exercise-cf-a
  (make-first-species cf-a
                      (make-melody
                       n/a4 n/g3  n/e3 n/a4
                       n/a4 n/c4 n/f4 n/e4
                       n/e3
                       n/g#3 n/a4)
                      :above))
(first-species->lily exercise-cf-a "treble")
(first-species-rules? exercise-cf-a)
(evaluate-species exercise-cf-c)
;; (sh/sh "timidity" "resources/temp.midi")

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

  ;;
  ;; generate 100 and sort them
  ;;
  (def position :above)
  (defn generate-species-eval [cf]
    (let [cp (generate-reverse-random-counterpoint position :g cf)
          species (make-first-species cf cp position)
          score (evaluate-species species)]
      [species score]))

  (def species100 (map (fn [_] (generate-species-eval boulanger-e)) (range 100)))

  (def unique-sorted-species100 (sort #(> (second %1) (second %2))
                                      (into [] (into #{} species100))))

  (def n 5) ;; number of best is 0
  (count unique-sorted-species100)
  (first-species->lily (first (nth unique-sorted-species100 n))
                       (if (= position :above) "treble" "treble_8"))
  (println (evaluate-species (first (nth unique-sorted-species100 n))))
  ;; (sh/sh "timidity" "resources/temp.midi")




  (let [cf salieri-c
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
