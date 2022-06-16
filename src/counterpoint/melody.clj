(ns counterpoint.melody
  (:require [counterpoint.core :refer [interval note->number-of-semitones]]
            [counterpoint.intervals :refer [get-interval]]
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

(defn transpose [melody octaves]
  (map #(make-note (get-note %) (+ (get-octave %) octaves) (get-acc %)) melody))

(defn- note->lily [note]
  (str " " 
       (name (get-note note))
       (case (get-acc note)
         :sharp "-sharp"
         :flat "-flat"
         "")
       (case (get-octave note)
         0 ",,"
         1 ","
         2 ""
         3 "'"
         4 "''"
         5 "'''")))

(defn- note->lily-relative [note previous]
  (let [interval-from-previous (get-interval (interval previous note))]
    (str " " 
         (name (get-note note))
         (case (get-acc note)
           :sharp "-sharp"
           :flat "-flat"
           "")
         (if (< (Math/abs interval-from-previous) 5)
           ""
           (if (pos? interval-from-previous) "'" ",")))))

(defn- to-lily-iter [previous note notes]

  (into [(if (nil? previous)
           (note->lily note)
           (note->lily-relative note previous))]
        (if (empty? notes)
          []
          (to-lily-iter note (first notes) (rest notes)))))

(defn melody->lily [[note & notes]]
  (apply str (to-lily-iter nil note notes)))

(comment
  (def melody (make-melody n/a4 n/c3 n/e3 n/e4 n/b4 n/a4))
  (def melody (make-melody n/a4 n/c3 n/e3 n/e4 n/g#3 n/a4))
  (def melody (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4))
  (def melody (make-melody n/d3 n/d3 n/a4 n/f3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3))

  melody
  (melody->lily melody)
  (transpose melody 1)

  (lowest-note melody)
  (highest-note melody)
  (melody-range melody)
  melody


  (nth melody (dec (dec (count melody))))

  (def final-interval (interval (nth melody (dec (dec (count melody))))
                                (last melody)))




  ;
  )
