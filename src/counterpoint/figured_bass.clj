(ns counterpoint.figured-bass 
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.melody :refer [double-melody]]))

(defn figured-bass-iter [low high lows highs]
  (println "figured-bass-iter" low high)
  (let [i (interval low high)
        in (get-interval i)
        bass (if (neg? in)
               (get-interval (interval high low))
               in)]
    (str "<" bass ">" (if (empty? lows)
                        ""
                        (figured-bass-iter (first lows) (first highs) (rest lows) (rest highs))))))

(defn figured-bass [first-species]
  (let [cantus (get-cantus first-species)
        counter (get-counter first-species)
        position (get-position first-species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter (first low) (first high) (rest low) (rest high))))


(defn figured-bass-second [species]
  (let [cantus (double-melody (get-cantus species))
        _ (println cantus)
        counter (get-counter species)
        position (get-position species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter (first low) (first high) (rest low) (rest high))))
