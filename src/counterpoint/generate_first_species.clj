(ns counterpoint.generate-first-species
  (:require [counterpoint.intervals :refer [m2 M2 m2- M2- m3 M3 m3- M3- m6
                                            note-at-diatonic-interval
                                            note-at-melodic-interval P4 P4- P5 P5- P8 P8-]]
            [counterpoint.melody :refer [make-melody ]]
            [counterpoint.notes :refer [get-nooctave] :as n]))

(defn next-candidates [key current-melody-note next-cantus-note]
  (let [next-melodic-candidates (map #(note-at-melodic-interval current-melody-note %)
                                     [m2 M2 m3 M3 P4 P5 m6 P8 m2- M2- m3- M3- P4- P5- P8-])
        next-harmonic-candidates
        (map #(note-at-diatonic-interval key (get-nooctave next-cantus-note) %) [1 3 5 6])]
    (filter #((set next-harmonic-candidates) (get-nooctave %)) next-melodic-candidates)))

(defn- generate-random-counterpoint-iter [key previous-melody note notes]
  (let [current (if (nil? previous-melody)
                  (note-at-melodic-interval note P8)
                  (rand-nth (next-candidates key previous-melody note)))]
    (into [current]
          (if (empty? notes)
            []
            (generate-random-counterpoint-iter key current (first notes) (rest notes))))))

(defn generate-random-counterpoint [key cantus]
  (generate-random-counterpoint-iter key nil (first cantus) (rest cantus)))

(comment

  (def haydn (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3))
  haydn


  (generate-random-counterpoint :c haydn)


  ;
  )