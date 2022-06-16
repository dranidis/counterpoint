(ns counterpoint.intervals)

(defn make-interval [n q]
  [n q])

(def P1 (make-interval 1 :perfect))
(def m2 (make-interval 2 :minor))
(def M2 (make-interval 2 :major))
(def m3 (make-interval 3 :minor))
(def M3 (make-interval 3 :major))
(def P4 (make-interval 4 :perfect))
(def P5 (make-interval 5 :perfect))
(def m6 (make-interval 6 :minor))
(def M6 (make-interval 6 :major))
(def m7 (make-interval 7 :minor))
(def M7 (make-interval 7 :major))
(def P8 (make-interval 8 :perfect))

(def m2- (make-interval -2 :minor))