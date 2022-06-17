(ns counterpoint.melody
  (:require [counterpoint.core :refer [interval note->number-of-semitones]]
            [counterpoint.notes :as n :refer [get-acc get-note get-octave
                                              make-note]]))

(defn make-melody [note & notes]
  (into [note] notes))

(defn- lowest-note [melody]
  (second (apply min-key #(note->number-of-semitones (second %)) (map-indexed vector melody))))

(defn- highest-note [melody]
  (second (apply max-key #(note->number-of-semitones (second %)) (map-indexed vector melody))))

(defn melody-range [melody]
  (interval (lowest-note melody)
            (highest-note melody)))

(defn last-interval [melody]
  (let [size (count melody)
        before-last (nth melody (dec (dec size)))
        last (last melody)]
    (interval before-last last)))

(defn- melodic-intervals-iter [note notes]
  (if (empty? notes)
    []
    (into [(interval note (first notes))]
          (melodic-intervals-iter (first notes) (rest notes)))))

(defn melodic-intervals [melody]
  (if (> (count melody) 1)
    (melodic-intervals-iter (first melody) (rest melody))
    []))

(defn transpose [melody octaves]
  (map #(make-note (get-note %) (+ (get-octave %) octaves) (get-acc %)) melody))


