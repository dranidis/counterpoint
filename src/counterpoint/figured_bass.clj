(ns counterpoint.figured-bass
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-low-high]]
            [counterpoint.intervals :refer [get-interval]]))

(defn- figured-bass [duration low high]
  (let [intval (interval low high)
        bass (cond
               (= intval :rest-interval) "_"
               (neg? (get-interval intval)) (get-interval (interval high low))
               :else (get-interval intval))]
    (str "<" bass ">" duration)))

(defn figured-bass-str [duration [low high]]
  (str
   "\\figures {"
   (apply str (map #(figured-bass duration %1 %2) low high))
   "}"))

(defn figured-bass-first [first-species]
  (figured-bass-str 1 (get-low-high first-species)))

(defn figured-bass-second [species]
  (figured-bass-str 2 (get-low-high species)))

(defn figured-bass-fourth [species]
  (figured-bass-str 2 (get-low-high species)))

