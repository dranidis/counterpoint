(ns counterpoint.generate-first-species
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval m2 M2 m2- M2- m3 M3
                                            m3- M3- m6 M6 m6-
                                            note-at-diatonic-interval note-at-melodic-interval P4 P4- P5 P5- P8 P8-]]
            [counterpoint.melody :refer [append-to-melody make-melody]]
            [counterpoint.motion :refer [direct-perfect?
                                         reverse-direct-perfect? type-of-motion]]
            [counterpoint.notes :refer [get-nooctave] :as n]))

(defn next-candidates [key melody previous-melody-note previous-cantus-note next-cantus-note]
  (let [next-melodic-candidates (map #(note-at-melodic-interval previous-melody-note %)
                                     [m2 M2 m3 M3 P4 P5 m6 P8 m2- M2- m3- M3- P4- P5- P8-])
        next-harmonic-candidates
        (map #(note-at-diatonic-interval key (get-nooctave next-cantus-note) %) [1 3 5 6])]
    (->> next-melodic-candidates
         (filter #((set next-harmonic-candidates) (get-nooctave %)))
         (filter #(not (direct-perfect? previous-cantus-note previous-melody-note next-cantus-note %)))
         (filter #(maximum-range-M10? (append-to-melody melody %))))))

(defn- generate-random-counterpoint-iter [key melody previous-melody previous-cantus cantus-note cantus-notes]
  (let [current (if (nil? previous-melody)
                  (note-at-melodic-interval cantus-note P8)
                  (rand-nth (next-candidates key melody previous-melody previous-cantus cantus-note)))]
    (into [current]
          (if (empty? cantus-notes)
            []
            (generate-random-counterpoint-iter key
                                               (into melody [current])
                                               current cantus-note (first cantus-notes) (rest cantus-notes))))))

(defn generate-random-counterpoint-above [key cantus]
  (generate-random-counterpoint-iter key [] nil nil (first cantus) (rest cantus)))


;;;;;;;;;;;;;;;;;;;;;;;;
;; REVERSE

(defn melody-reverse-leap
  ;; TODO
  [melody]
  (let [i (get-interval (interval (second melody) (first melody)))]
    (cond
      (> i 3) :low
      (< i 3) :high
      :else :no-leap)))

;; TODO avoid dim5 in melody of leaps or at changing direction
(defn next-reverse-candidates [key melody m36s previous-melody-note previous-cantus-note next-cantus-note]
  (let [next-melodic-intervals (case (melody-reverse-leap melody)
                                 :high [m2 M2 m3 M3 m2- M2- m3- M3-]
                                 :low [m2 M2 m3 M3 m2- M2- m3- M3-]
                                 [m2 M2 m3 M3 P4 P5 P8 m2- M2- m3- M3- P4- P5- m6- P8-])
        next-melodic-candidates (map #(note-at-melodic-interval previous-melody-note %)
                                     next-melodic-intervals)
        next-harmonic-intervals (cond (= 3 (get m36s :thirds)) [1 5 6]
                                      (= 3 (get m36s :sixths)) [1 3 5]
                                      :else [1 3 5 6])
        next-harmonic-candidates
        (map #(note-at-diatonic-interval key (get-nooctave next-cantus-note) %) next-harmonic-intervals)]
    (->> next-melodic-candidates
         (filter #((set next-harmonic-candidates) (get-nooctave %)))
         (filter #(pos? (get-interval (interval next-cantus-note %))))
         (filter #(not (reverse-direct-perfect? previous-cantus-note previous-melody-note next-cantus-note %)))
         (filter #(maximum-range-M10? (append-to-melody melody %))))))



(defn- generate-reverse-random-counterpoint-iter [key melody m36s previous-melody previous-cantus cantus-note cantus-notes]
  (println (reverse melody))
  (let [current (if (nil? previous-melody)
                  (note-at-melodic-interval cantus-note P8)
                  (rand-nth (next-reverse-candidates key melody m36s previous-melody previous-cantus cantus-note)))
        m36' (case (get-interval (interval cantus-note current))
               3 (-> m36s (update :thirds inc) (assoc :sixths 0))
               6 (-> m36s (update :sixths inc) (assoc :thirds 0))
               (-> m36s (assoc :thirds 0) (assoc :sixths 0)))]
    (into [current]
          (if (empty? cantus-notes)
            []
            (generate-reverse-random-counterpoint-iter key
                                                       (into melody [current])
                                                       m36'
                                                       current cantus-note (first cantus-notes) (rest cantus-notes))))))

(defn generate-reverse-random-counterpoint-above [key cantus]
  (let [rev-cantus (reverse cantus)
        last-melody (note-at-melodic-interval (first rev-cantus) P8)
        second-last-melody (note-at-melodic-interval (second rev-cantus) M6)]
    (into (into [] (reverse (generate-reverse-random-counterpoint-iter key
                                                                       [last-melody second-last-melody]
                                                                       {:thirds 0 :sixths 1} ;; counter of thirds & sixths
                                                                       second-last-melody
                                                                       (second rev-cantus)
                                                                       (nth rev-cantus 2)
                                                                       (subvec (into [] rev-cantus) 3))))
          [second-last-melody last-melody])))



(comment

  (def haydn (make-melody n/d3 n/e3 n/f3 n/d3 n/a4 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3))
  haydn

  (nth haydn 2)
  (subvec haydn 3)
  (reverse haydn)

  (into [1 2 3] [4 5])


  (generate-reverse-random-counterpoint-above :c haydn)


  ;
  )