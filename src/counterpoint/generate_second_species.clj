(ns counterpoint.generate-second-species
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.generate-first-species :refer [crossing-filter
                                                         dim-or-aug-filter
                                                         ending-first max-harmonic-interval next-harmonic-intervals
                                                         next-melodic-intervals-reverse]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?
                                            next-diatonic
                                            note-at-diatonic-interval note-at-melodic-interval P12- P5 P5- prev-diatonic]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.notes :refer [get-nooctave]]
            [counterpoint.rest :as rest]
            [counterpoint.second-species :refer [undisguised-direct-motion-of-downbeats-to-perfect]]))

(defn- ending-second [position rev-cantus]
  (let [octave? (> (rand-int 10) 4)
        [last-melody second-last-melody m36s] (ending-first position rev-cantus)
        third-last-melody (note-at-melodic-interval (second rev-cantus)
                                                    (if (= position :above)
                                                      (if octave? P5 P5)
                                                      (if octave? P12- P5-)))]
    [last-melody second-last-melody third-last-melody m36s]))



(defn next-candidate-notes [position key melody m36s previous-melody previous-cantus cantus-note]
  (let [next-melodic-candidates (map
                                 #(note-at-melodic-interval previous-melody %)
                                 (next-melodic-intervals-reverse melody))
        next-harmonic-candidates (map
                                  #(note-at-diatonic-interval key (get-nooctave cantus-note) %)
                                  (next-harmonic-intervals position m36s))
        next-candidates (->> next-melodic-candidates
                             (filter #(not= previous-melody %))
                             (filter (dim-or-aug-filter position cantus-note))
                             (filter (crossing-filter position cantus-note))
                             (filter #((set next-harmonic-candidates) (get-nooctave %)))
                             (filter #(maximum-range-M10? (append-to-melody melody %)))
                             (filter #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval)))]
    next-candidates))

(defn- next-reverse-downbeat-candidates [position key melody m36s
                                         previous-melody previous-cantus cantus-note]
  ;; (println "d MELODY" (reverse melody))
  ;; (println "d PREV MEL" previous-melody)
  ;; (println "d PREV CAN" previous-cantus)
  ;; (println "d cantus note" cantus-note)
  (let [downbeat-cands (if (= (get m36s :remaining-cantus-size) 1)
                         [rest/r]
                         (next-candidate-notes position key melody m36s
                                               previous-melody previous-cantus cantus-note))
        ;; _ (println "downbeat-cands" downbeat-cands)
        ]
    downbeat-cands))


(defn passing-tones [key previous-melody cantus-note]
  (let [p1 (note-at-diatonic-interval key (get-nooctave previous-melody) 3)
        p2 (note-at-diatonic-interval key (get-nooctave previous-melody) -3)
        next-harmonic-candidates (set (map
                                       #(note-at-diatonic-interval key (get-nooctave cantus-note) %)
                                       [1 3 5 6]))
        p1? (next-harmonic-candidates p1)
        p2? (next-harmonic-candidates p2)]
    (remove nil?
            (-> []
                (conj (if p1? [(next-diatonic key previous-melody)
                               (next-diatonic key (next-diatonic key previous-melody))] nil))
                (conj (if p2? [(prev-diatonic key previous-melody)
                               (prev-diatonic key (prev-diatonic key previous-melody))] nil))))))

(defn- next-reverse-candidates [position key melody m36s
                                previous-melody previous-cantus cantus-note]
  (let [upbeat-candidates (next-candidate-notes position key melody m36s
                                                previous-melody previous-cantus cantus-note)
        ;; _ (println "upbeat-candidates" upbeat-candidates)
        candidates (reduce
                    #(into %1 %2) []
                    (map (fn [upc]
                           (for [downbeat
                                 (next-reverse-downbeat-candidates position
                                                                   key
                                                                   (into  melody [upc])
                                                                   m36s
                                                                   upc
                                                                   previous-cantus
                                                                   cantus-note)
                                 upbeat [upc]]
                             (vector upbeat downbeat)))
                         upbeat-candidates))
        passing (if (> (get m36s :remaining-cantus-size) 1)
                  (passing-tones key previous-melody cantus-note)
                  nil)
        result 
        (remove nil?
                       (conj (->> candidates
         ;; add filter for parallel fifths (cannot be disguised)
                                  (filter (fn [[u d]]
                                            (let [cantus-bar [cantus-note cantus-note previous-cantus]
                                                  counter-bar [d u previous-melody]]
                    ;;  (and 
                                              (not (undisguised-direct-motion-of-downbeats-to-perfect cantus-bar counter-bar))
                      ;; (not (direct-motion-to-perfect? cantus-note u previous-cantus previous-melody))
                      ;; )
                                              ))))
                             passing))
        ;; _ (println "Cand before removing errors" result)
        ]
    result))

(defn- update-m36-size [m36s position cantus-note current]
  (update m36s :remaining-cantus-size dec))

(defn- generate-reverse-random-counterpoint-second-iter
  [position key melody m36s previous-melody previous-cantus cantus-note cantus-notes]
  ;; (println "MELODY" (reverse melody))
  ;; (println "PREV MEL" previous-melody)
  ;; (println "PREV CAN" previous-cantus)
  ;; (println "cantus note" cantus-note)
  ;; (println "CANTUS NOTES" cantus-notes)
  (let [candidates (next-reverse-candidates
                    position key melody m36s previous-melody previous-cantus cantus-note)
        ;; _ (println "CANDIDATES" candidates)
        _ (when (= 1 (count candidates))
            (println "FORCED"))
        current (rand-nth candidates)
        ;; _ (println "CHOSEN" current)
        ]
    ;; (into current
    (if (empty? cantus-notes)
      (into melody current)
      (generate-reverse-random-counterpoint-second-iter
       position
       key
       (into melody current)
       (update-m36-size m36s position cantus-note current)
       (second current)
       cantus-note (first cantus-notes) (rest cantus-notes)))))
            ;;  )

(defn generate-reverse-random-counterpoint-second [position key cantus]
  (try
    (let [rev-cantus (reverse cantus)
          [last-melody second-last-melody third-last-melody m36s]
          (ending-second position rev-cantus)
          melody [last-melody second-last-melody third-last-melody]
          generated (reverse (generate-reverse-random-counterpoint-second-iter
                              position
                              key
                              melody
                              m36s ;; counter of thirds & sixths
                              third-last-melody
                              (second rev-cantus)
                              (nth rev-cantus 2)
                              (subvec (into [] rev-cantus) 3)))
          ;; _ (println "GEN" generated)
          ]
      (into [] generated))
    (catch Exception _
      (println "Trying again...")
      (generate-reverse-random-counterpoint-second position key cantus))))
