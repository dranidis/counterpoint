(ns counterpoint.fourth-species
  (:require [counterpoint.core :refer [simple-interval]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?
                                            P1 P5 P8]]
            [counterpoint.melody :refer [double-melody melody-motion-score
                                         melody-score]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position make-species]]
            [counterpoint.utils :refer [rule-warning]]))

(defn make-fourth-species [cantus-firmus counterpoint-melody position]
  (make-species cantus-firmus counterpoint-melody position :fourth))

(defn get-low-high-fourth [species]
  (let [counter (get-counter species)
        double-cantus (double-melody (get-cantus species))]
    (if (= (get-position species) :above)
      [double-cantus counter]
      [counter double-cantus])))

(defmethod get-low-high :fourth
  [species]
  (get-low-high-fourth species))

(defn consonant-upbeats? [species]
  (let [[low high] (get-low-high species)]
    (every? harmonic-consonant?
            (map second (partition 2 (map simple-interval low high))))))

(defn consonant-or-resolving-downbeats? [species]
  (let [[low high] (get-low-high species)
        downbeat-intervals (rest (map first (partition 2 (map simple-interval low high))))
        melody-pairs (rest (partition 2 (if (= (get-position species) :above) high low)))
        melody-intervals (map simple-interval
                              (map first melody-pairs)
                              (map second melody-pairs))]
    (every? identity (map (fn [x y]
                            (or (harmonic-consonant? x)
                                (= -2 (get-interval y)))) downbeat-intervals melody-intervals))))


;; upbeats are always consonant
;; Dissonant ligatures must resolve downward by step to a consonance
;; allowed suspensions: 
;;  upper voice: 4-3, 7-6, 9-8 (not often 2-1)
;;  lower voice: 2-3, 4-5, 9-10 (no 7-8)
;; Ligatures cannot be used to disguise voice leading errors 
;; After a consonant ligaure usually a leap to a consonant downbeat

;; two perfect consonances of the same interval must not be separated
;; only by a dissonant suspension.

;; ending: upper 7-6 suspension, lower 2-3

(defn fourth-species-rules? [species]
  (and
   (rule-warning (=  (inc (* 2 (dec (count (get-cantus species)))))
                     (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
   (allowed-melodic-intervals? species)
   (rule-warning (consonant-upbeats? species)
                 #(println "Upbeats are not consonant!"))
   (rule-warning (consonant-or-resolving-downbeats? species)
                 #(println "Check downbeats! Must be consonant or resolving"))))

(defn- number-of-downbeat-dissonances [species]
  (let [[low high] (get-low-high species)
        downbeat-intervals (rest
                            (map first
                                 (partition 2
                                            (map simple-interval low high))))]
    (count (filter #(not (harmonic-consonant? %)) downbeat-intervals))))

(defn- number-of-suspensions [species]
  (reduce
   #(+ %1 (if (= (first %2) (second %2)) 1 0))
   0
   (partition 2 (rest (get-counter species)))))

(defn evaluate-fourth-species
  [species & {:keys [verbose]
              :or {verbose false}}]
  (let [size (count (get-cantus species))
        [low high] (get-low-high species)
        harm-int (map simple-interval low high)
        [p1-count p8-count p5-count]
        (map (fn [int]
               (count (filter #(= int %) harm-int)))
             [P1 P8 P5])
        ;; cp-ints (melodic-intervals (get-counter species))
        ;; ca-ints (melodic-intervals (get-cantus species))
        ;; simult-leaps (simultaneous-leaps ca-ints cp-ints)

        score-harmony (+ (* -100 p1-count)
                         (* -50 p8-count)
                         (* -20 p5-count)
                ;;  (* -20 simult-leaps)
                         )
        susp (number-of-suspensions species)
        diss (number-of-downbeat-dissonances species)
        species-score (+ (* 100 diss) (* 100 susp))
        melody-s (melody-score
                  (filter #(not= % :rest)
                          (get-counter species)) :verbose verbose)
        norm (fn [score] (float (/ score size)))
        motion-score (melody-motion-score low high)]
    (when verbose
      ;;  (println "L " (melodic-intervals-all low) (count (melodic-intervals-all low)))
      ;;  (println "H " (melodic-intervals-all high) (count (melodic-intervals-all high)))
      (println "Motion score" motion-score)
      (println "Melody score" (norm melody-s))
      (println "Harmony score" (norm score-harmony))
      (println "Num of suspensions diss/total"
               diss "/" susp)
      (println "Species score" (norm species-score)))
    (norm (+
           (* 10 species-score)
           (* 10 motion-score)
           score-harmony
           (* 10 melody-s)))))



