(ns counterpoint.melody
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval get-quality
                                            note->number-of-semitones]]
            [counterpoint.notes :as n :refer [get-acc get-note get-octave
                                              make-note]]
            [counterpoint.rest :refer [rest?]]))

(defn make-melody [note & notes]
  (into [note] notes))

(defn append-to-melody [melody notes]
  (into melody notes))

(defn insert-to-melody [note melody]
  (into [note] melody))

(defn- lowest-note [melody]
  (second (apply min-key #(note->number-of-semitones (second %)) (map-indexed vector melody))))

(defn highest-note [melody]
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
  (let [mel (append-to-melody melody [note note])]
    (if (empty? notes)
      mel
      (double-melody-iter mel (first notes) (rest notes)))))

(defn double-melody [melody]
  (double-melody-iter [] (first melody) (rest melody)))

(defn- quad-melody-iter [melody note notes]
  (let [mel (append-to-melody melody [note note note note])]
    (if (empty? notes)
      mel
      (quad-melody-iter mel (first notes) (rest notes)))))

(defn quad-melody [melody]
  (quad-melody-iter [] (first melody) (rest melody)))

(defn remove-last
  ([melody]
   (remove-last melody 1))
  ([melody num-elements]
   (subvec melody 0 (- (count melody) num-elements))))

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

(defn- detect-up-downs [intervals state]
  (if (empty? intervals)
    (:count state)
    (let [up-2? (= 2 (get-interval (first intervals)))
          dn-2? (= -2 (get-interval (first intervals)))]
      (case (:state state)
        :init (if up-2?
                (detect-up-downs (rest intervals) (assoc state :state :asc1))
                (detect-up-downs (rest intervals) state))
        :asc1 (if up-2?
                (detect-up-downs (rest intervals) (assoc state :state :asc2))
                (detect-up-downs (rest intervals) (assoc state :state :init)))
        :asc2 (if up-2?
                (detect-up-downs (rest intervals) state)
                (if dn-2?
                  (detect-up-downs (rest intervals) (assoc state :state :desc))
                  (detect-up-downs (rest intervals) (assoc state :state :init))))
        :desc (if up-2?
                (detect-up-downs (rest intervals) (assoc state :state :asc1))
                (if dn-2?
                  (detect-up-downs (rest intervals) (update
                                                     (assoc state :state :init)
                                                     :count inc))
                  (detect-up-downs (rest intervals)
                                   (assoc state :state :init))))))))
(defn get-up-downs [melody]
  (let [intervals (melodic-intervals melody)
        up-downs (detect-up-downs intervals {:state :init :count 0})]
    up-downs))

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
        peaks (count (filter #(= % (highest-note melody)) melody))
        up-downs (get-up-downs melody)
        _ (println "UP-DNs" up-downs)
        ;; _ (when (> skeleton-diminished 0)
        ;;     (println "Diminished interval in melody skeleton"))
        score (+ (* -20 (dec leaps))
                 (* -50 unisons)
                 (* -2 thirds)
                 (* -500 skeleton-diminished)
                 (* -200 up-downs)
                 (* -50 (dec peaks)))]
    score))

(comment
  (def diminished-melody (make-melody n/d4 n/g4 n/f4 n/g4 n/a5 n/b5 n/a5))
  (melodic-intervals (melody-skeleton diminished-melody))
  (melody-score diminished-melody)
  ;
  )