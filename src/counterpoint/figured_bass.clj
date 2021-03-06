(ns counterpoint.figured-bass
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-low-high get-type]]
            [counterpoint.intervals :refer [get-interval]]))

(defn- figured-bass-interval [duration low high]
  (let [intval (interval low high)
        bass (cond
               (= intval :rest-interval) "_"
               (neg? (get-interval intval)) (get-interval (interval high low))
               :else (get-interval intval))]
    (str "<" bass ">" duration)))

(defn figured-bass-str [duration [low high]]
  (str
   "\\figures {"
   (apply str (map #(figured-bass-interval duration %1 %2) low high))
   "}"))

(defmulti figured-bass get-type)

(defmethod figured-bass :first
 [species]
 (figured-bass-str 1 (get-low-high species)))

(defmethod figured-bass :second
  [species]
  (figured-bass-str 2 (get-low-high species)))

(defmethod figured-bass :fourth
  [species]
  (figured-bass-str 2 (get-low-high species)))


