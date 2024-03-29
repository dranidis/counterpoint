(ns counterpoint.melody
  (:require [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval get-quality
                                            note->number-of-semitones]]
            [counterpoint.notes :as n :refer [get-acc get-note get-octave
                                              make-note]]
            [counterpoint.rest :refer [rest?]]
            [counterpoint.utils :as utils]))

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

(defn melodic-intervals-all [melody]
  (if (> (count melody) 1)
    (melodic-intervals-iter (first melody) (rest melody))
    []))

(defn melodic-intervals-no-rest [melody]
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

;; (defn remove-last
;;   ([melody]
;;    (remove-last melody 1))
;;   ([melody num-elements]
;;    (subvec melody 0 (- (count melody) num-elements))))

(def remove-last utils/remove-last)

(defn- melody-skeleton-iter  [intv intvs melody]
  (if (empty? intvs)
    [(first melody)]
    (into
     (if (neg? (* (get-interval intv) (get-interval (first intvs))))
       [(first melody)]
       [])
     (melody-skeleton-iter (first intvs) (rest intvs) (rest melody)))))

(defn melody-skeleton [melody]
  (let [ints (melodic-intervals-no-rest melody)]
    (into [(first melody)] (melody-skeleton-iter (first ints) (rest ints) (rest melody)))))

(defn- detect-up-downs [intervals state up-down?]
  (if (empty? intervals)
    (:count state)
    (let [interv (get-interval (first intervals))
          up-2? (= (if up-down? 2 -2) interv)
          dn-2? (= (if up-down? -2 2) interv)]
      (case (:state state)
        :init (if up-2?
                (detect-up-downs (rest intervals)
                                 (assoc state :state :asc1) up-down?)
                (detect-up-downs (rest intervals) state up-down?))
        :asc1 (if up-2?
                (detect-up-downs (rest intervals)
                                 (assoc state :state :asc2) up-down?)
                (detect-up-downs (rest intervals)
                                 (assoc state :state :init) up-down?))
        :asc2 (if up-2?
                (detect-up-downs (rest intervals) state up-down?)
                (if dn-2?
                  (detect-up-downs (rest intervals)
                                   (assoc state :state :desc) up-down?)
                  (detect-up-downs (rest intervals)
                                   (assoc state :state :init) up-down?)))
        :desc (if up-2?
                (detect-up-downs (rest intervals)
                                 (assoc state :state :asc1) up-down?)
                (if dn-2?
                  (detect-up-downs (rest intervals) (update
                                                     (assoc state :state :init)
                                                     :count inc) up-down?)
                  (detect-up-downs (rest intervals)
                                   (assoc state :state :init) up-down?)))))))
(defn get-up-downs [melody]
  (let [intervals (melodic-intervals-no-rest melody)
        up-downs (detect-up-downs intervals {:state :init :count 0} true)]
    up-downs))

(defn get-down-ups [melody]
  (let [intervals (melodic-intervals-no-rest melody)
        up-downs (detect-up-downs intervals {:state :init :count 0} false)]
    up-downs))

(defn melody-score [melody & {:keys [verbose]
                              :or {verbose false}}]
  (let [ints (map #(Math/abs (get-interval %)) (melodic-intervals-no-rest melody))
        leaps (count (filter #(> % 3) ints))
        unisons (count (filter #(= % 1) ints))
        thirds (count (filter #(= % 3) ints))
        skeleton-diminished (count
                             (filter
                              #(or (= :aug (get-quality %))
                                   (= :dim (get-quality %)))
                              (melodic-intervals-no-rest (melody-skeleton melody))))
        peaks (count (filter #(= % (highest-note melody)) melody))
        up-downs (get-up-downs melody)
        down-ups (get-down-ups melody)
        m-range (get-interval (melody-range melody))
        range-score (* -100 (Math/abs (- m-range 10)))
        score (+ (* -20 (dec leaps))
                 (* -50 unisons)
                 (* -2 thirds)
                 (* -500 skeleton-diminished)
                 (* -200 (+ up-downs down-ups))
                 (* -50 (dec peaks))
                 range-score)]
    (when verbose
      (println "Melody evalution")
      (println "-- UP-DNs" up-downs)
      (println "-- DN-UPs" down-ups)
      (println "-- MEL RANGE" m-range)
      ;; (println (melody-skeleton melody))
      (when (> skeleton-diminished 0)
        (println "-- Diminished interval in melody skeleton")))
    (float (/ score 50))))

(defn melody-motion-score [low high]
  (* -1 (reduce (fn [total [li hi]]
                  (+ total (* (Math/abs (get-interval li))
                              (Math/abs (get-interval hi)))))
                0
                (->> (mapv (fn [li hi] [li hi])
                           (melodic-intervals-all low)
                           (melodic-intervals-all high))
                     (filter (fn [[li hi]] (and (not= li :rest-interval)
                                                (not= hi :rest-interval))))
                     (filter (fn [[li hi]]
                               (let [[ili ihi] (map get-interval [li hi])]
                                 (or (>  (* ili ihi) 0) ;; similar motion
                                     ;; simult skips (similar or contrary)
                                     (and (> (Math/abs ili) 2)
                                          (> (Math/abs ihi) 2))))
                              ;;  (and (not= 1 (get-interval li))
                              ;;                   (not= 1 (get-interval hi)))
                               ))))))

(comment
  (def diminished-melody (make-melody n/d4 n/g4 n/f4 n/g4 n/a5 n/b5 n/a5))
  (melodic-intervals-no-rest (melody-skeleton diminished-melody))
  (melody-score diminished-melody)
  ;
  )