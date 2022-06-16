(ns counterpoint.cantus
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [A10 get-interval
                                            interval-less-than-or-equal? make-interval]]
            [counterpoint.melody :refer [melody-range]]))

(defn make-cantus-firmus [key melody]
  [key melody])

(defn get-key [[key _]] key)
(defn get-melody [[_ melody]] melody)

(defn ending-note? [cantus]
  (let [melody (get-melody cantus)
        key (get-key cantus)]
    (= key (last melody))))

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

(defn rule-warning [rule message]
  (when (not rule)
    (prn message))
  rule)

(defn- no-tone-repetitions-helper? [note notes]
  (if (empty? notes)
    true
    (and (not= note (first notes))
         (no-tone-repetitions-helper? (first notes) (rest notes)))))

(defn- no-tone-repetitions? [[note & notes]]
  (no-tone-repetitions-helper? note notes))

(defn cantus-rules? [cantus]
  (let [melody (get-melody cantus)]
    (and (rule-warning (maximum-range-M10? melody) (str "Maximum range violation! " (melody-range melody)))
         (rule-warning (first-last-same melody) "First & Last note violation!")
         (rule-warning (final-note-approach? melody) "Second to last note interval violation!")
         (rule-warning (ending-note? cantus) "Ending note violation!")
         (rule-warning (no-tone-repetitions? melody) "Tone repetitions violation!"))))

