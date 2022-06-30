(ns counterpoint.first-species-type)

(defn  make-first-species [cantus-firmus counterpoint-melody position]
  [cantus-firmus counterpoint-melody position])

(defn get-cantus [[c _ _]] c)
(defn get-counter [[_ m _]] m)
(defn get-position [[_ _ p]] p)

(defn get-low-high [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)]
    (if (= (get-position species) :above)
      [counter cantus]
      [cantus counter])))