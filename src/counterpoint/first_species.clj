(ns counterpoint.first-species
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.intervals :refer [get-interval get-quality
                                            harmonic-consonant? m6 make-interval P1 P8
                                            P8-]]
            [counterpoint.melody :refer [last-interval
                                         melodic-intervals-no-rest melody-motion-score melody-score]]
            [counterpoint.motion :refer [type-of-motion]]
            [counterpoint.rest :refer [rest?]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-low-high get-position harmony-score]]
            [counterpoint.utils :refer [rule-warning]]))

(defn  make-first-species [cantus-firmus counterpoint-melody position]
  [cantus-firmus counterpoint-melody position :first])

(defn correct-interval [note1 note2]
  (if (or (rest? note1) (rest? note2))
    true
    (let [harmony (simple-interval note1 note2)
          pi (interval note1 note2)
          interval (Math/abs (get-interval harmony))
          result (case (get-quality harmony)
                   :perfect (not= interval 4)
                   :minor (or (= interval 3)
                              (= interval 6))
                   :major (or (= interval 3)
                              (= interval 6))
                   false)]
      (rule-warning
       result
       #(str "Not allowed harmonic interval "
             note1 note2 pi)))))

(defn correct-intervals-iter [position note1 note2 notes1 notes2]
  (and (if (= position :above)
         (rule-warning (correct-interval note1 note2)
                       #(str "Not allowed harmonic interval " note1 note2 (interval note1 note2)))
         (rule-warning (correct-interval note2 note1)
                       #(str "Not allowed harmonic interval " note2 note1 (interval note2 note1))))
       (if (empty? notes1)
         true
         (correct-intervals-iter position (first notes1) (first notes2) (rest notes1) (rest notes2)))))

(defn correct-intervals [species]
  (let [[low high] (get-low-high species)]
    (every? (fn [i]
              (let [res (harmonic-consonant? i)]
                (rule-warning res #(str "Not allowed harmonic interval " i))))
            (map simple-interval low high))))

(defn last-interval? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        last (if (= position :above)
               (simple-interval (last cantus) (last counter))
               (simple-interval (last counter) (last cantus)))]
    (rule-warning (or (= last P1) (= last P8)) #(str "Last interval not a P1 or P8 " last))))

(defn ending? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        last-cantus (last-interval cantus)
        last-counter (last-interval counter)]
    (rule-warning (or (and (= last-cantus (make-interval -2 :major))
                           (= last-counter (make-interval 2 :minor)))
                      (and (= last-cantus (make-interval -2 :minor))
                           (= last-counter (make-interval 2 :major)))
                      (and (= last-cantus (make-interval 2 :major))
                           (= last-counter (make-interval -2 :minor)))
                      (and (= last-cantus (make-interval 2 :minor))
                           (= last-counter (make-interval -2 :major))))
                  #(str "Does not approach the ending by contrary motion and leading tone " last-cantus last-counter))))

(defn direct-motion-to-perfect? [n1 n2 next1 next2]
  (if (or (rest? n1) (rest? n2) (rest? next1) (rest? next2))
    false
    (and
     (= :similar (type-of-motion n1 n2 next1 next2))
     (= (get-quality (interval next1 next2)) :perfect))))

(defn- no-direct-motion-to-perfect-iter [n1 n2 notes1 notes2]
  (if (empty? notes1)
    true
    (let [next1 (first notes1)
          next2 (first notes2)]
      (and
       (rule-warning (not (direct-motion-to-perfect? n1 n2 next1 next2))
                     #(str "Direct motion to perfect interval from " n1 n2 " to " next1 next2 " intervals: "
                           (interval n1 n2) (interval next1 next2)))
       (no-direct-motion-to-perfect-iter next1 next2 (rest notes1) (rest notes2))))))

(defn no-direct-motion-to-perfect? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (if (= position :above)
      (no-direct-motion-to-perfect-iter (first cantus) (first counter) (rest cantus) (rest counter))
      (no-direct-motion-to-perfect-iter (first counter) (first cantus) (rest counter) (rest cantus)))))

(defn- allowed-melodic-interval [interval]
  (if (= interval :rest-interval)
    true
    (rule-warning
     (or (<= (Math/abs (get-interval interval)) 5)
         (= interval m6)
         (= interval P8)
         (= interval P8-))
     #(str "Not allowed interval in melody: " interval))))

(defn allowed-melodic-intervals-iter? [counter-intervals]
  (if (empty? counter-intervals)
    true
    (and (allowed-melodic-interval (first counter-intervals))
         (allowed-melodic-intervals-iter? (rest counter-intervals)))))

(defn allowed-melodic-intervals? [species]
  (rule-warning
   (allowed-melodic-intervals-iter? (melodic-intervals-no-rest (get-counter species)))
   #(str "Not allowd interval in melody: " (get-counter species))))

(defn first-species-rules? [species]
  (and
   (rule-warning (= (count (get-cantus species)) (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
   (last-interval? species)
   (ending? species)
   (allowed-melodic-intervals? species)
   (correct-intervals species)
   (no-direct-motion-to-perfect? species)
;;    avoid consecutive perfect intervals
   ))

(defn evaluate-first-species [species & {:keys [verbose]
                                         :or {verbose false}}]
  (let [[low high] (get-low-high species)
        score (harmony-score low high)
        melody-s (melody-score (get-counter species)
                               :verbose verbose)
        motion-score (melody-motion-score low high)
        total (float (/ (+ score
                           melody-s
                           motion-score) (count (get-cantus species))))]
    (when verbose
      (println "1st species evalution")
      (println "-- HARMONY SCORE" score)
      (println "-- MELODY SCORE" melody-s)
      (println "-- MOTION SCORE" motion-score))
    total
    ;; {:harm score :motion-score motion-score :mel melody-s :total total}
    ;;
    ))
;; avoid consecutive perfect harmonic intervals
;; prefer stepwise movement

