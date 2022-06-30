(ns counterpoint.figured-bass
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-low-high]]
            [counterpoint.fourth-species :refer [get-low-high-fourth]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.second-species :refer [get-low-high-second]]))

(defn- figured-bass [duration low high]
  (let [intval (interval low high)
        bass (cond
               (= intval :rest-interval) "_"
               (neg? (get-interval intval)) (get-interval (interval high low))
               :else (get-interval intval))]
    (str "<" bass ">" duration)))

(defn figured-bass-first [first-species]
  (let [duration 1
        [high low] (get-low-high first-species)]
    (apply str (map #(figured-bass duration %1 %2) low high))))

(defn figured-bass-second [species]
  (let [duration 2
        [high low] (get-low-high-second species)]
    (apply str (map #(figured-bass duration %1 %2) low high))))

(defn figured-bass-fourth [species]
  (let [duration 2
        [high low] (get-low-high-fourth species)]
    (apply str (map #(figured-bass duration %1 %2) low high))))

