(ns counterpoint.fifth-species
  (:require [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position make-species]]))

;; Quadruple meter
;;
;; Dissonant passing tones can occur at half-note level on beat 3
;; or a quarter-note level at beats 2, 3 and 4.
;;
;; Dissonant neighbor tones can occur as quarter notes on
;; beats 2 3.

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

(defn counter-cantus 
  "Returns a copy of the counter notes and durations
   with all notes replaced
   by notes of the cantus"
  [counter cantus]
  (map #(replace-notes-with %1 %2) cantus counter))

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
