(ns counterpoint.species-type
  (:require [counterpoint.core :refer [simple-interval]]
            [counterpoint.intervals :refer [harmonic-consonant? P1 P5 P8]]
            [counterpoint.utils :refer [remove-last]]))

(defn  make-species [cantus-firmus counterpoint-melody position type]
  [cantus-firmus counterpoint-melody position type])



(defn get-cantus [[c _ _ _]] c)
(defn get-counter [[_ m _ _]] m)
(defn get-position [[_ _ p _]] p)
(defn get-type [[_ _ _ t]] t)

(defn- get-low-high-first [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)]
    (if (= (get-position species) :above)
      [cantus counter]
      [counter cantus])))

(defmulti get-low-high get-type)

(defmethod get-low-high :first
  [species]
  (get-low-high-first species))

(defn get-harmonic-intervals [species]
  (let [[low high] (get-low-high species)]
    (mapv simple-interval low high)))

(defn harmony-score [low high]
  (let [harm-int (rest 
                  (remove-last 
                   (mapv simple-interval low high)
                   )
                  )
        [p1-count p8-count p5-count]
        (map (fn [int]
               (count (filter #(= int %) harm-int)))
             [P1 P8 P5])

        score (+ (* -20 p1-count)
                 (* -20 p8-count)
                 (* -10 p5-count))]
    score))

(defn dissonance-score [low high]
  (let [harm-int (rest
                  (remove-last
                   (mapv simple-interval low high)))
        diss (count (remove harmonic-consonant? harm-int))
        score-diss (* 5 diss)]
    score-diss))