(ns counterpoint.generate-second-species
  (:require [counterpoint.generate-first-species :refer [crossing-filter
                                                         dim-or-aug-filter
                                                         ending-first next-harmonic-intervals next-melodic-intervals-reverse]]
            [counterpoint.intervals :refer [note-at-diatonic-interval
                                            note-at-melodic-interval P12- P5 P5-]]
            [counterpoint.notes :refer [get-nooctave]]
            [counterpoint.rest :as rest]))

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
                             (filter #((set next-harmonic-candidates) (get-nooctave %))))]
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

(defn- next-reverse-candidates [position key melody m36s
                                previous-melody previous-cantus cantus-note]
  (let [upbeat-candidates (next-candidate-notes position key melody m36s
                                                previous-melody previous-cantus cantus-note)
        ;; _ (println "upbeat-candidates" upbeat-candidates)
        ]
    (reduce
     #(into %1 %2)
     []
     (map (fn [upc] (for [downbeat
                          (next-reverse-downbeat-candidates position
                                                            key
                                                            (into  melody [upc])
                                                            m36s
                                                            upc
                                                            previous-cantus
                                                            cantus-note)
                          upbeat [upc]]
                      (vector upbeat downbeat)))
          upbeat-candidates))))

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
      (generate-reverse-random-counterpoint-second position key cantus)
      )))

