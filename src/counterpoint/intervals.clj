(ns counterpoint.intervals
(:require [counterpoint.key :refer [get-note-acc-at-key]]
          [counterpoint.notes :refer [get-not-nooctave make-note-nooctave
                                      note->num num->note]]))

(defn make-interval [n q]
  [n q])

(defn get-interval [[n _]] n)
(defn get-quality [[_ q]] q)

(defn interval-less-than-or-equal? [interval1 interval2]
  (<= (Math/abs (get-interval interval1))
     (Math/abs (get-interval interval2))))

(defn note-at-diatonic-interval [key note interval]
  (let [note-num (note->num (get-not-nooctave note))
        note' (num->note (+ note-num interval (if (pos? interval) -1 1)))
        accidental (get-note-acc-at-key key note')
        ]
    (make-note-nooctave note' accidental))
  )

(def P1 (make-interval 1 :perfect))
(def m2 (make-interval 2 :minor))
(def M2 (make-interval 2 :major))
(def m3 (make-interval 3 :minor))
(def M3 (make-interval 3 :major))
(def P4 (make-interval 4 :perfect))
(def P5 (make-interval 5 :perfect))
(def m6 (make-interval 6 :minor))
(def M6 (make-interval 6 :major))
(def m7 (make-interval 7 :minor))
(def M7 (make-interval 7 :major))
(def P8 (make-interval 8 :perfect))
(def m9 (make-interval 9 :minor))
(def M9 (make-interval 9 :major))

(def m10 (make-interval 10 :minor))
(def M10 (make-interval 10 :major))
(def P11 (make-interval 11 :perfect))
(def P12 (make-interval 12 :perfect))
(def m13 (make-interval 13 :minor))
(def M13 (make-interval 13 :major))
(def m14 (make-interval 14 :minor))
(def M14 (make-interval 14 :major))
(def P15 (make-interval 15 :perfect))

(def d2 (make-interval 2 :dim))
(def d3 (make-interval 3 :dim))
(def d4 (make-interval 4 :dim))
(def d5 (make-interval 5 :dim))
(def d6 (make-interval 6 :dim))
(def d7 (make-interval 7 :dim))
(def d8 (make-interval 8 :dim))

(def A1 (make-interval 1 :aug))
(def A2 (make-interval 2 :aug))
(def A3 (make-interval 3 :aug))
(def A4 (make-interval 4 :aug))
(def A5 (make-interval 5 :aug))
(def A6 (make-interval 6 :aug))
(def A7 (make-interval 7 :aug))
(def A8 (make-interval 8 :aug))
(def A9 (make-interval 9 :aug))
(def A10 (make-interval 10 :aug))

(def m2- (make-interval -2 :minor))
(def M2- (make-interval -2 :major))
(def m3- (make-interval -3 :minor))
(def M3- (make-interval -3 :major))
(def P4- (make-interval -4 :perfect))
(def P5- (make-interval -5 :perfect))
(def m6- (make-interval -6 :minor))
(def M6- (make-interval -6 :major))
(def m7- (make-interval -7 :minor))
(def M7- (make-interval -7 :major))
(def P8- (make-interval -8 :perfect))

(def d2- (make-interval -2 :dim))
(def d3- (make-interval -3 :dim))
(def d4- (make-interval -4 :dim))
(def d5- (make-interval -5 :dim))
(def d6- (make-interval -6 :dim))
(def d7- (make-interval -7 :dim))
(def d8- (make-interval -8 :dim))

(def A1- (make-interval -1 :aug))
(def A2- (make-interval -2 :aug))
(def A3- (make-interval -3 :aug))
(def A4- (make-interval -4 :aug))
(def A5- (make-interval -5 :aug))
(def A6- (make-interval -6 :aug))
(def A7- (make-interval -7 :aug))
