(ns counterpoint.rest)

(defn make-rest []
  :rest)

(defn rest? [note]
  (= :rest note))

(def r (make-rest))

