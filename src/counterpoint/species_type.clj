(ns counterpoint.species-type)

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
      [counter cantus]
      )))

(defmulti get-low-high get-type)

(defmethod get-low-high :first
 [species]
  (get-low-high-first species)
 )