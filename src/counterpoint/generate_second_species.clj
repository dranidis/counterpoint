(ns counterpoint.generate-second-species
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval]]
            [counterpoint.first-species :refer [direct-motion-to-perfect?]]
            [counterpoint.generate-first-species :refer [crossing-filter
                                                         dim-or-aug-filter
                                                         max-harmonic-interval next-harmonic-intervals next-melodic-intervals-reverse]]
            [counterpoint.intervals :refer [get-interval m10- m3- M6
                                            next-diatonic note-at-diatonic-interval
                                            note-at-melodic-interval P1 P12- P5 P5- P8 P8- prev-diatonic]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.notes :refer [get-nooctave]]
            [counterpoint.rest :as rest]
            [counterpoint.second-species :refer [undisguised-direct-motion-of-downbeats-to-perfect]]))

(defn ending-first-revised [octave? position rev-cantus]
  (let [
        last-melody (note-at-melodic-interval (first rev-cantus)
                                              (if (= position :above)
                                                (if octave? P8 P8)
                                                (if octave? P8- P1)))
        second-last-melody (note-at-melodic-interval (second rev-cantus)
                                                     (if (= position :above)
                                                       (if octave? M6 M6)
                                                       (if octave? m10- m3-)))
        rem-cantus (- (count rev-cantus) 2)
        m36s (if (= position :above)
               {:thirds 0 :sixths 1 :tens 0 :thirteens 0 :remaining-cantus-size rem-cantus}
               (if octave?
                 {:thirds 0 :sixths 0 :tens 1 :thirteens 0 :remaining-cantus-size rem-cantus}
                 {:thirds 1 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size rem-cantus}))]
    [last-melody second-last-melody m36s]))


(defn- ending-second [position rev-cantus]
  (let [octave? (> (rand-int 10) 4)
        [last-melody second-last-melody m36s] (ending-first-revised octave? position rev-cantus)
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
                             (filter #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval))

                            ;;  haydn-a is not solvable
                             ;; is this examined twice??
                             (filter #(not (direct-motion-to-perfect? cantus-note % previous-cantus previous-melody))))]
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
                                               previous-melody previous-cantus cantus-note))]
    downbeat-cands))

(defn passing-tones [position key previous-melody cantus-note]
  (let [p1 (note-at-diatonic-interval key (get-nooctave previous-melody) 3)
        p2 (note-at-diatonic-interval key (get-nooctave previous-melody) -3)
        next-harmonic-candidates (set (map
                                       #(note-at-diatonic-interval key (get-nooctave cantus-note) %)
                                       (if (= position :above)
                                         [1 3 5 6]
                                         [1 -3 -5 -6])))
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
        candidates (reduce
                    #(into %1 %2) []
                    (map (fn [upc]
                           (for [downbeat
                                 (next-reverse-downbeat-candidates position
                                                                   key
                                                                   (into  melody [upc])
                                                                   m36s
                                                                   upc
                                                                   ;; same note in cantus (held)
                                                                   cantus-note
                                                                   cantus-note)
                                 upbeat [upc]]
                             (vector upbeat downbeat)))
                         upbeat-candidates))
        allowed-unisons? false
        passing (->> (if (> (get m36s :remaining-cantus-size) 1)
                       (passing-tones position key previous-melody cantus-note)
                       nil)
                     (filter (fn [[p2 p1]]
                               (and ((crossing-filter position cantus-note
                                                      allowed-unisons?) p1)
                                    ((dim-or-aug-filter position cantus-note) p1)))))]
    (->> (remove nil? (into candidates passing))
         (filter (fn [[upbeat downbeat]]
                   (let [cantus-bar [cantus-note cantus-note previous-cantus]
                         counter-bar [downbeat upbeat previous-melody]]
                     (and
                      (not (undisguised-direct-motion-of-downbeats-to-perfect cantus-bar counter-bar))
                      (not (direct-motion-to-perfect? cantus-note upbeat previous-cantus previous-melody)))))))))

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
        ;; _ (case (count candidates)
        ;;     1 (println "FORCED" candidates)
        ;;     0 (println "No candidates"))
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
      (println "\nTrying again...")
      (generate-reverse-random-counterpoint-second position key cantus))))


