(ns counterpoint.cantus
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [A10 get-interval
                                            interval-less-than-or-equal? make-interval]]
            [counterpoint.melody :refer [melody-range]]))

(defn maximum-range-M10? [melody]
  (interval-less-than-or-equal? (melody-range melody) A10))

(defn first-last-same [melody]
  (= (first melody) (last melody)))

(defn final-note-approach? [melody]
  (if (< (count melody) 2)
    false
    (let [final-interval (interval (nth melody (dec (dec (count melody))))
                                   (last melody))]
      (or (= (get-interval final-interval) -2)
          (= final-interval (make-interval 2 :minor))))))