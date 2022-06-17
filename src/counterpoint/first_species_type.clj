(ns counterpoint.first-species-type)

(defn  make-first-species [cantus-firmus counterpoint-melody position]
  [cantus-firmus counterpoint-melody position])

(defn get-cantus [[c _ _]] c)
(defn get-counter [[_ m _]] m)
(defn get-position [[_ _ p]] p)