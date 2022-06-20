(ns counterpoint.generate-first-species
  (:require [counterpoint.intervals :refer [m2 M2 m2- M2- m3 M3 m3- M3- m6
                                            note-at-diatonic-interval
                                            note-at-melodic-interval P4 P4- P5 P5- P8 P8-]]
            [counterpoint.notes :refer [a3 b4 get-nooctave]]))

(defn next-candidates [current-melody-note next-cantus-note]
  (let [next-melodic-candidates (map #(note-at-melodic-interval current-melody-note %)
                                     [m2 M2 m3 M3 P4 P5 m6 P8 m2- M2- m3- M3- P4- P5- P8-])
        next-harmonic-candidates
        (map #(note-at-diatonic-interval :c (get-nooctave next-cantus-note) %) [1 3 5 6])]
    (filter #((set next-harmonic-candidates) (get-nooctave %)) next-melodic-candidates)))

(comment

  (next-candidates b4 a3)

  ;
  )