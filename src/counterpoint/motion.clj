(ns counterpoint.motion
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval get-quality]]
            [counterpoint.notes :as n]))

(defn type-of-motion [first1 first2 next1 next2]
  (let [interval1 (get-interval (interval first1 next1))
        interval2 (get-interval (interval first2 next2))]
    (cond (or (= interval1 1)
              (= interval2 1)) :oblique
          (neg? (* interval1 interval2)) :contrary
          :else :similar)))

(defn direct-perfect? [first1 first2 next1 next2]
  (and (= :perfect (get-quality (interval next1 next2)))
       (= :similar (type-of-motion first1 first2 next1 next2))))

(defn reverse-direct-perfect? [first1 first2 next1 next2]
  (and (= :perfect (get-quality (interval first1 first2)))
       (= :similar (type-of-motion first1 first2 next1 next2))))

(comment
  
  (type-of-motion n/f3 n/f3 n/a4 n/f4)
  
  (type-of-motion n/c3 n/c4 n/e3 n/g3)
  (type-of-motion n/e3 n/c4 n/a4 n/c4)
  (type-of-motion n/e3 n/c4 n/e3 n/d4)
  (type-of-motion n/e3 n/c4 n/f3 n/d4)
  (type-of-motion n/e3 n/c4 n/d3 n/b4)

  (direct-perfect? n/c3 n/d4 n/e3 n/e4)
  (direct-perfect? n/c3 n/d4 n/e3 n/f4)

  (reverse-direct-perfect? n/g3 n/g4 n/e3 n/g3)
  ;
  )
