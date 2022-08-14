(ns counterpoint.fifth-species
  (:require [counterpoint.cantus :refer [get-melody]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position make-species]]))

(defn make-fifth-species [cantus-firmus counterpoint-melody position]
  (make-species cantus-firmus counterpoint-melody position :fifth))

(defn- replace-notes-with
  "Returns a copy of the counter-notes-duration with all notes replaced by cantus-note"
  [cantus-note counter-notes-duration]
  ;; (println cantus-note counter-notes-duration)
  (map #(let [n (:n %)
              c (map (fn [_] cantus-note) n)]
          (assoc % :n c))
       counter-notes-duration))

(defn- multiply
  "Doubles or triples or .... the cantus note depending on the number of lists in each
   measure in the counter"
  [cantus counter]
  cantus)

(defn counter-cantus [counter cantus]
  (map #(replace-notes-with %1 %2) (multiply cantus counter) counter))

(defn- get-low-high-fifth [species]
  (let [counter (get-counter species)
        cantus (counter-cantus counter (get-cantus species))]
    (if (= (get-position species) :above)
      [cantus counter]
      [counter cantus])))

(defmethod get-low-high :fifth
  [species]
  (get-low-high-fifth species))

(defn evaluate-fifth-species [species & {:keys [verbose]
                                         :or {verbose false}}]
  -999)

(defn fifth-species-rules? [species]
  true)
