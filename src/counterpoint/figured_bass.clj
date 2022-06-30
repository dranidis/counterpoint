(ns counterpoint.figured-bass
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.melody :refer [double-melody insert-to-melody]]
            [counterpoint.rest :refer [rest?]]
            [counterpoint.rest :as rest]))

(defn figured-bass-iter [duration low high lows highs]
  (if (or (nil? high)
          (nil? low))
    (str "< _ >" duration)
    (let [bass (if (or (rest? high) (rest? low))
                 "_"
                 (let [in (get-interval (interval low high))]
                   (if (neg? in)
                     (get-interval (interval high low))
                     in)))]
      (str "<" bass ">"
           duration (if (empty? lows)
                      ""
                      (figured-bass-iter duration (first lows) (first highs) (rest lows) (rest highs)))))))

(defn figured-bass [first-species]
  (let [duration 1
        cantus (get-cantus first-species)
        counter (get-counter first-species)
        position (get-position first-species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter duration (first low) (first high) (rest low) (rest high))))


(defn figured-bass-second [species]
  (let [duration 2
        cantus (double-melody (get-cantus species))
        counter (get-counter species)
        position (get-position species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter duration (first low) (first high) (rest low) (rest high))))

(defn figured-bass-fourth [species]
  (let [duration 2
        cantus (double-melody (get-cantus species))
        counter (insert-to-melody rest/r (get-counter species))
        position (get-position species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter duration (first low) (first high) (rest low) (rest high))))

