(ns counterpoint.first-species-examples
  (:require [clojure.java.shell :as sh]
            [counterpoint.intervals :refer [get-interval get-quality m6
                                            make-interval P1 P8 P8-]]
            [counterpoint.cantus-firmi-examples :refer [haydn-a]]
            [counterpoint.first-species :refer [first-species-rules?
                                                get-harmonic-intervals
                                                evaluate-species]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [generate-reverse-random-counterpoint-above]]
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

  (def species (make-first-species
                haydn-a
                (generate-reverse-random-counterpoint-above :c haydn-a) :above))

  P1
  (count (filter #(= P1 %) (get-harmonic-intervals species)))

  (first-species-rules? species)

  (def cf haydn-a)
  (def cp (generate-reverse-random-counterpoint-above :c cf))
  (def species  (make-first-species cf cp :above))

  (println (first-species-rules? species))
  (println (evaluate-species species))


  (defn generate-species-eval [cf]
    (let [cp (generate-reverse-random-counterpoint-above :c cf)
          species (make-first-species cf cp :above)
          score (evaluate-species species)]
      [species score]))
  (range 20)

  (def species100 (map (fn [_] (generate-species-eval haydn-a)) (range 100)))

  (def best-of-100 (first (first (filter #(= (second %) (apply max (map second species100))) species100))))
  (def worst-of-100 (first (first (filter #(= (second %) (apply min (map second species100))) species100))))

  (first-species->lily best-of-100)
  (evaluate-species best-of-100)

  (first-species->lily worst-of-100)
  (evaluate-species worst-of-100)
  (sh/sh "timidity" "resources/temp.midi")



  (let [cf haydn-a
        cp (generate-reverse-random-counterpoint-above :c cf)
        _ (println (let [ints (map #(Math/abs (get-interval %)) (melodic-intervals cp))
                         leaps (count (filter #(> % 3) ints))
                         unisons (count (filter #(= % 1) ints))
                         thirds (count (filter #(= % 3) ints))
                         score (+ (* -5 leaps)
                                  (* -10 unisons)
                                  (* -2 thirds))]
                     [score leaps unisons thirds]))
        species (make-first-species cf cp :above)
        _ (println "RULES " (first-species-rules? species))
        _ (println "EVAL  " (evaluate-species species))]
    (first-species->lily species))
  (sh/sh "timidity" "resources/temp.midi")

  (get-harmonic-intervals species)

  (print (first-species->lily species))

  (filter #(> % 3) [5 2 3 2 1 2 3 3 3 2])
  ;
  )
