(ns counterpoint.fourth-species
  (:require [counterpoint.core :refer [simple-interval]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-low-high
                                                     get-position make-species]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?]]
            [counterpoint.melody :refer [double-melody]]
            [counterpoint.utils :refer [rule-warning]]))

(defn make-fourth-species [cantus-firmus counterpoint-melody arg3]
  (make-species cantus-firmus counterpoint-melody arg3 :fourth))

(defn get-low-high-fourth [species]
  (let [counter (get-counter species)
        double-cantus (double-melody (get-cantus species))]
    (if (= (get-position species) :above)
      [double-cantus counter]
      [counter double-cantus])))

(defmethod get-low-high :fourth
  [species]
  (get-low-high-fourth species))

(defn consonant-upbeats? [species]
  (let [[low high] (get-low-high species)]
    (every? harmonic-consonant?
            (map second (partition 2 (map simple-interval low high))))))

(defn consonant-or-resolving-downbeats? [species]
  (let [[low high] (get-low-high species)
        downbeat-intervals (rest (map first (partition 2 (map simple-interval low high))))
        melody-pairs (rest (partition 2 (if (= (get-position species) :above) high low)))
        melody-intervals (map simple-interval
                              (map first melody-pairs)
                              (map second melody-pairs))]
    (every? identity (map (fn [x y]
                            (or (harmonic-consonant? x)
                                (= -2 (get-interval y)))) downbeat-intervals melody-intervals))))


;; upbeats are always consonant
;; Dissonant ligatures must resolve downward by step to a consonance
;; allowed suspensions: 
;;  upper voice: 4-3, 7-6, 9-8 (not often 2-1)
;;  lower voice: 2-3, 4-5, 9-10 (no 7-8)
;; Ligatures cannot be used to disguise voice leading errors 
;; After a consonant ligaure usually a leap to a consonant downbeat

;; two perfect consonances of the same interval must not be separated
;; only by a dissonant suspension.

;; ending: upper 7-6 suspension, lower 2-3

(defn fourth-species-rules? [species]
  (and
   (rule-warning (=  (inc (* 2 (dec (count (get-cantus species)))))
                     (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
   (allowed-melodic-intervals? species)
   (rule-warning (consonant-upbeats? species)
                 #(println "Upbeats are not consonant!"))
   (rule-warning (consonant-or-resolving-downbeats? species)
                 #(println "Check downbeats! Must be consonant or resolving"))))



