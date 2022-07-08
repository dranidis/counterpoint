(ns counterpoint.melody
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval get-quality
                                            note->number-of-semitones]]
            [counterpoint.notes :as n :refer [get-acc get-note get-octave
                                              make-note]]
            [counterpoint.rest :refer [rest?]]))

(defn make-melody [note & notes]
  (into [note] notes))

(defn append-to-melody [melody note]
  (into melody [note]))

(defn insert-to-melody [note melody]
  (into [note] melody))

(defn- lowest-note [melody]
  (second (apply min-key #(note->number-of-semitones (second %)) (map-indexed vector melody))))

(defn- highest-note [melody]
  (second (apply max-key #(note->number-of-semitones (second %)) (map-indexed vector melody))))

(defn melody-range [melody]
  (interval (lowest-note melody)
            (highest-note melody)))

(defn last-interval [melody]
  (let [size (count melody)
        before-last (nth melody (dec (dec size)))
        last (last melody)]
    (interval before-last last)))

(defn- melodic-intervals-iter [note notes]
  (if (empty? notes)
    []
    (into [(interval note (first notes))]
          (melodic-intervals-iter (first notes) (rest notes)))))

(defn melodic-intervals [melody]
  (filter #(not= :rest-interval %)
          (if (> (count melody) 1)
            (melodic-intervals-iter (first melody) (rest melody))
            [])))

(defn transpose [melody octaves]
  (map #(if (rest? %)
          %
          (make-note (get-note %) (+ (get-octave %) octaves) (get-acc %))) melody))

(defn- double-melody-iter [melody note notes]
  (let [mel (-> melody
                (append-to-melody note)
                (append-to-melody note))]
    (if (empty? notes)
      mel
      (double-melody-iter mel (first notes) (rest notes)))))

(defn double-melody [melody]
  (double-melody-iter [] (first melody) (rest melody)))

(defn remove-last [melody]
  (subvec melody 0 (dec (count melody))))

(defn- melody-skeleton-iter  [intv intvs melody]
  (if (empty? intvs)
    [(first melody)]
    (into
     (if (neg? (* (get-interval intv) (get-interval (first intvs))))
       [(first melody)]
       [])
     (melody-skeleton-iter (first intvs) (rest intvs) (rest melody)))))

(defn melody-skeleton [melody]
  (let [ints (melodic-intervals melody)]
    (into [(first melody)] (melody-skeleton-iter (first ints) (rest ints) (rest melody)))))


(defn melody-score [melody]
  (let [ints (map #(Math/abs (get-interval %)) (melodic-intervals melody))
        leaps (count (filter #(> % 3) ints))
        unisons (count (filter #(= % 1) ints))
        thirds (count (filter #(= % 3) ints))
        skeleton-diminished (count
                             (filter
                              #(or (= :aug (get-quality %))
                                   (= :dim (get-quality %)))
                              (melodic-intervals (melody-skeleton melody))))
        ;; _ (when (> skeleton-diminished 0)
        ;;     (println "Diminished interval in melody skeleton"))
        score (+ (* -5 leaps)
                 (* -10 unisons)
                 (* -2 thirds)
                 (* -100 skeleton-diminished))]
    score))

(comment
  (def diminished-melody (make-melody n/d4 n/g4 n/f4 n/g4 n/a5 n/b5 n/a5))
  (melodic-intervals (melody-skeleton diminished-melody))
  (melody-score diminished-melody)
  ;
  )