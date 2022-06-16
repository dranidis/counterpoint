(ns counterpoint.core 
  (:require [counterpoint.notes :refer [get-acc get-note get-octave]]))


(defn- note->number [note]
  (let [num-note (case (get-note note)
                   :a 0
                   :b 1
                   :c 2
                   :d 3
                   :e 4
                   :f 5
                   :g 6)]
    (+ num-note (* (get-octave note) 7))))

(defn- note->number-of-semitones [note]
  (let [num-note (case (get-note note)
                   :a 0
                   :b 2
                   :c 3
                   :d 5
                   :e 7
                   :f 8
                   :g 10)
        acc->num (case (get-acc note)
                   :natural 0
                   :flat -1
                   :sharp +1)]
    (+ (* (get-octave note) 12) num-note acc->num)))

(defn- distance-in-semitones [note1 note2]
  (- (note->number-of-semitones note2)
     (note->number-of-semitones note1)))

(defn- interval-quality [octave-distance distance-semitones]
  (case octave-distance
    1 (case distance-semitones
        -1 :dim 0 :perfect 1 :aug
        11 :dim 12 :perfect 13 :augnil)
    2 (case distance-semitones 0 :dim 1 :minor 2 :major 3 :aug nil)
    3 (case distance-semitones 2 :dim 3 :minor 4 :major 5 :aug nil)
    4 (case distance-semitones 4 :dim 5 :perfect 6 :aug nil)
    5 (case distance-semitones 6 :dim 7 :perfect 8 :aug nil)
    6 (case distance-semitones 7 :dim 8 :minor 9 :major 10 :aug nil)
    7 (case distance-semitones 9 :dim 10 :minor 11 :major 12 :aug nil)
    ;; 8 (case distance-semitones 11 :dim 12 :perfect 13 :aug nil)
    nil))

(defn interval [note1 note2]
  (let [distance (- (note->number note2) (note->number note1))
        octave-distance (Math/abs (if (> distance 0)
                          (inc (mod distance 7))
                          (dec (mod distance -7))))
        distance-semitones (Math/abs (if (> distance 0)
                                       (mod (distance-in-semitones note1 note2) 12)
                                       (mod (distance-in-semitones note1 note2) -12)))
        quality (interval-quality octave-distance distance-semitones)]
    [(if (>= distance 0)
       (inc distance)
       (dec distance)) 
     quality]))


