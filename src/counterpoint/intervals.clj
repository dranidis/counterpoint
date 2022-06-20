(ns counterpoint.intervals
  (:require [counterpoint.key :refer [get-note-acc-at-key]]
            [counterpoint.notes :refer [a3 c4 get-acc get-nooctave get-note
                                        get-note-nooctave get-octave make-note
                                        make-note-nooctave note->num num->note num2->note]]))

(defn make-interval [n q]
  [n q])

(defn get-interval [[n _]] n)
(defn get-quality [[_ q]] q)

(defn note->number-of-semitones [note]
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

(defn distance-in-semitones [note1 note2]
  (- (note->number-of-semitones note2)
     (note->number-of-semitones note1)))

(defn interval-less-than-or-equal? [interval1 interval2]
  (<= (Math/abs (get-interval interval1))
      (Math/abs (get-interval interval2))))

(defn note-at-diatonic-interval [key note-nooctave interval]
  (let [note-num (note->num (get-note-nooctave note-nooctave))
        note' (num->note (+ note-num interval (if (pos? interval) -1 1)))
        accidental (get-note-acc-at-key key note')]
    (make-note-nooctave note' accidental)))

(defn- semitones-of-interval [interval]
  (let [i (Math/abs (get-interval interval))
        q (get-quality interval)
        compound (* (quot (dec i) 7) 12)
        simple (inc (mod (dec i) 7))
        result (+ compound
                  (case simple
                    1 (case q
                        :dim -1
                        :perfect 0
                        :aug 1)
                    2 (case q
                        :dim 0
                        :minor 1
                        :major 2
                        :aug 3)
                    3 (case q
                        :dim 2
                        :minor 3
                        :major 4
                        :aug 5)
                    4 (case q
                        :dim 4
                        :perfect 5
                        :aug 6)
                    5 (case q
                        :dim 6
                        :perfect 7
                        :aug 8)
                    6 (case q
                        :dim 7
                        :minor 8
                        :major 9
                        :aug 10)
                    7 (case q
                        :dim 9
                        :minor 10
                        :major 11
                        :aug 12)))]
    result))

(defn- raise-note [note]
  (let [n (get-note note)
        o (get-octave note)
        a (get-acc note)
        new-acc (case a
                  :double-flat :flat
                  :flat :natural
                  :natural :sharp
                  :sharp :double-sharp
                  nil)]
    (if (nil? new-acc)
      (throw (Exception. (str "raise-note: " note " not possible")))
      (make-note n o new-acc))))

(raise-note [:c 4 :natural])

(defn- flatten-note [note]
  (let [n (get-note note)
        o (get-octave note)
        a (get-acc note)
        new-acc (case a
                  :flat :doulbe-flat
                  :natural :flat
                  :sharp :natural
                  :double-sharp :sharp
                  nil)]
    (if (nil? new-acc)
      (throw (Exception. (str "flatten-note: " note " not possible")))
      (make-note n o new-acc))))

(defn note-at-melodic-interval [note interval]
  (let [i (get-interval interval)
        note-num (note->num (get-note note))
        [note' o-diff] (num2->note (+ note-num i (if (pos? i) -1 1)))
        o (+ (get-octave note) o-diff)
        new-note (make-note note' o :natural)
        semitones (distance-in-semitones note new-note)
        sem-diff (- (* (if (neg? i) -1 1) (semitones-of-interval interval)) semitones)
        result (case sem-diff
                 -2 (flatten-note (flatten-note new-note))
                 -1 (flatten-note new-note)
                 0 new-note
                 1 (raise-note new-note)
                 2 (raise-note (raise-note new-note))
                 nil)]
    (if (nil? result)
     (throw (Exception. (str "note-at-melodic-interval: No case for " sem-diff " semitones!"))) 
      result)))


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

