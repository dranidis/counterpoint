(ns counterpoint.first-species-type)

(defn  make-species [cantus-firmus counterpoint-melody position type]
  [cantus-firmus counterpoint-melody position type])

(defn  make-first-species [cantus-firmus counterpoint-melody position]
  [cantus-firmus counterpoint-melody position :first])

(defn get-cantus [[c _ _ _]] c)
(defn get-counter [[_ m _ _]] m)
(defn get-position [[_ _ p _]] p)
(defn get-type [[_ _ _ t]] t)

(defn get-low-high [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)]
    (if (= (get-position species) :above)
      [counter cantus]
      [cantus counter])))