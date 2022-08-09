(ns counterpoint.third-species
  (:require [counterpoint.melody :refer [quad-melody remove-last]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position make-species]]))

(defn make-third-species [cantus-firmus counterpoint-melody arg3]
  (make-species cantus-firmus counterpoint-melody arg3 :third))

(defn- get-low-high-third [species]
  (let [counter (get-counter species)
        quad-cantus (remove-last
                     (quad-melody (get-cantus species))
                     3)]
    (if (= (get-position species) :above)
      [quad-cantus counter]
      [counter quad-cantus])))

(defmethod get-low-high :third
  [species]
  (get-low-high-third species))

(defn evaluate-third-species [species]
  -999)

(defn third-species-rules? [species]
  false)
