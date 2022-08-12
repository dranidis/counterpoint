(ns counterpoint.fifth-species 
  (:require [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position make-species]]))

(defn make-fifth-species [cantus-firmus counterpoint-melody position]
  (make-species cantus-firmus counterpoint-melody position :fifth))

(defn- get-low-high-fifth [species]
  (let [counter (get-counter species)
        cantus (get-cantus species)]
    (if (= (get-position species) :above)
      [cantus counter]
      [counter cantus])))

(defmethod get-low-high :fifth
  [species]
  (get-low-high-fifth species))

(defn evaluate-fifth-species [species]
  -999)

(defn fifth-species-rules? [species]
  true)
