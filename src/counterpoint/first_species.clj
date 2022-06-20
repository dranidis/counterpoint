(ns counterpoint.first-species
  (:require [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.first-species-type :refer [get-cantus get-counter get-position]]
            [counterpoint.intervals :refer [get-interval get-quality m6 make-interval P1 P8]]
            [counterpoint.melody :refer [last-interval melodic-intervals]]
            [counterpoint.utils :refer [rule-warning]]))

(defn- correct-interval [note1 note2]
  (let [harmony (simple-interval note1 note2)
        interval (Math/abs (get-interval harmony))]
    (case (get-quality harmony)
      :perfect (not= interval 4)
      :minor (or (= interval 3)
                 (= interval 6))
      :major (or (= interval 3)
                 (= interval 6))
      false)))

(defn- correct-intervals-iter [position note1 note2 notes1 notes2]
  (and (if (= position :above)
         (rule-warning (correct-interval note1 note2)
                       #(str "Not allowed harmonic interval " note1 note2 (interval note1 note2)))
         (rule-warning (correct-interval note2 note1)
                       #(str "Not allowed harmonic interval " note2 note1 (interval note2 note1))))
       (if (empty? notes1)
         true
         (correct-intervals-iter position (first notes1) (first notes2) (rest notes1) (rest notes2)))))

(defn correct-intervals [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (if (not= (count cantus) (count counter))
      false
      (correct-intervals-iter position (first cantus) (first counter) (rest cantus) (rest counter)))))

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

(defn- no-direct-motion? [n1 n2 next1 next2]
  (let [interval1 (get-interval (simple-interval n1 next1))
        interval2 (get-interval (simple-interval n2 next2))]
    (rule-warning (or (= interval1 1)
                      (= interval2 1)
                      (neg? (* interval1 interval2))) #(str "Direct motion from " n1 n2 " to " next1 next2 " intervals: " (interval n1 n2) (interval next1 next2)))))

(defn- no-direct-motion-to-perfect-iter [position n1 n2 notes1 notes2]
  (if (empty? notes1)
    true
    (let [next1 (first notes1)
          next2 (first notes2)
          next-interval (interval next1 next2)]
      (and (or (not= (get-quality next-interval) :perfect)
               (if (= position :above)
                 (no-direct-motion? n1 n2 next1 next2)
                 (no-direct-motion? n2 n1 next2 next1)))
           (no-direct-motion-to-perfect-iter position next1 next2 (rest notes1) (rest notes2))))))


(defn no-direct-motion-to-perfect? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (no-direct-motion-to-perfect-iter position (first cantus) (first counter) (rest cantus) (rest counter))))

(defn- allowed-melodic-interval [interval]
  (rule-warning
   (or (<= (Math/abs (get-interval interval)) 5)
       (= interval m6)
       (= interval P8))
   #(str "Not allowed interval in melody: " interval)))

(Math/abs 3)

(defn allowed-melodic-intervals-iter? [counter-intervals]
  (if (empty? counter-intervals)
    true
    (and (allowed-melodic-interval (first counter-intervals))
         (allowed-melodic-intervals-iter? (rest counter-intervals)))))

(defn allowed-melodic-intervals? [species]
  (allowed-melodic-intervals-iter? (melodic-intervals (get-counter species))))

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

(defn figured-bass-iter [low high lows highs]
  (str "<" (get-interval (interval low high)) ">" (if (empty? lows)
                                                    ""
                                                    (figured-bass-iter (first lows) (first highs) (rest lows) (rest highs)))))

(defn figured-bass [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        [high low] (if (= position :above)
                     [counter cantus]
                     [cantus counter])]
    (figured-bass-iter (first low) (first high) (rest low) (rest high))))



