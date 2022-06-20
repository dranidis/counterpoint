(ns counterpoint.core
  (:require [counterpoint.intervals :refer [get-interval get-quality
                                            make-interval distance-in-semitones]]
            [counterpoint.notes :refer [get-note get-octave note->num]]
            ))


(defn- note->number [note]
  (let [num-note (note->num (get-note note))]
    (if (nil? num-note)
      (throw (Exception. (str "note->number: Unknown note: " note)))
      (+ num-note (* (get-octave note) 7)))))



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
    (make-interval (if (>= distance 0)
                     (inc distance)
                     (dec distance))
                   quality)))

(defn simple-interval [note1 note2]
  (let [compound (interval note1 note2)
        distance (get-interval compound)]
    (make-interval (if (> distance 7)
                     (mod distance 7)
                     distance)
                   (get-quality compound))))



