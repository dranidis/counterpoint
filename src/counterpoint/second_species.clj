(ns counterpoint.second-species
  (:require [counterpoint.core :refer [interval note->number]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?
                                                correct-interval
                                                correct-intervals-iter direct-motion-to-perfect?]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-low-high get-position
                                                     make-species]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.melody :refer [double-melody melody-score
                                         remove-last]]
            [counterpoint.motion :refer [type-of-motion]]
            [counterpoint.notes :as n]
            [counterpoint.rest :refer [rest?]]
            [counterpoint.utils :refer [rule-warning]]))

(defn make-second-species [cantus-firmus counterpoint-melody arg3]
  (make-species cantus-firmus counterpoint-melody arg3 :second))

(defn get-low-high-second [species]
  (let [counter (get-counter species)
        double-cantus (remove-last (double-melody (get-cantus species)))]
    (if (= (get-position species) :above)
      [double-cantus counter]
      [counter double-cantus])))

(defmethod get-low-high :second
  [species]
  (get-low-high-second species))

(defn- downbeats-iter [downbeats-melody rest]
  (cond (empty? rest) downbeats-melody
        (= 1 (count rest)) (into downbeats-melody rest)
        :else (let [next-downbeat (first rest)
                    rest-melody (subvec (into [] rest) 2)]
                (downbeats-iter (into downbeats-melody [next-downbeat]) rest-melody))))

(defn downbeats-second [melody]
  (downbeats-iter [] melody))

;; SECOND SPECIES
;;
;; downbeat considerations
;;   only consonant intervals 
;; upbeat considerations
;;   consonant 
;;   or dissonant passing tones
;; NOT two perfect 5ths on consecutive downbeats (disguised parallel fifths)

;; directed 5ths (second interval a 5th)
;;   a large leap (4th) disguises the directed 5ths

;; first measure can start on second half

;; end measure 5 6 -> 8  or  5 3 -> 8

(defn correct-downbeat-intervals [species]
  (let [cantus (get-cantus species)
        counter (downbeats-second (get-counter species))
        position (get-position species)]
    (if (not= (count cantus) (count counter))
      false
      (correct-intervals-iter position (first cantus) (first counter) (rest cantus) (rest counter)))))


(defn- first-three [melody]
  (subvec melody 0 3))

(defn- rest-bars [melody]
  (subvec melody 2))


(defn passing-tone [[n1 n2 n3]]
  (if (or (rest? n1) (rest? n2) (rest? n3))
    false
    (let [note1 (note->number n1)
          note2 (note->number n2)
          note3 (note->number n3)]
      (or (and (= (inc note1) note2)
               (= (inc note2) note3))
          (and (= note2 (inc note3))
               (= note1 (inc note2)))))))

(passing-tone [n/g4 n/f4 n/e4])

(defn- correct-upbeat-interval [position cantus-bar counter-bar]
  (or (passing-tone counter-bar)
      (let [note1 (second cantus-bar)
            note2 (second counter-bar)]
        (if (= position :above)
          (rule-warning (correct-interval note1 note2)
                        #(str "Not allowed harmonic interval (or passing tone) " counter-bar note1 note2 (interval note1 note2)))
          (rule-warning (correct-interval note2 note1)
                        #(str "Not allowed harmonic interval (or passing tone) " counter-bar note2 note1 (interval note2 note1)))))))

(defn- correct-upbeat-intervals-iter [position cantus-bar counter-bar rest-cantus rest-counter]
  (and (correct-upbeat-interval position cantus-bar counter-bar)
       (if (< (count rest-cantus) 2)
         true
         (correct-upbeat-intervals-iter position
                                        (first-three rest-cantus)
                                        (first-three rest-counter)
                                        (rest-bars rest-cantus)
                                        (rest-bars rest-counter)))))

(defn correct-upbeat-intervals [species]
  (let [double-cantus (remove-last (double-melody (get-cantus species)))
        counter (get-counter species)
        position (get-position species)]
    (if (not= (count double-cantus) (count counter))
      (rule-warning false #(println "Wrong size of counterpoint"))
      (correct-upbeat-intervals-iter position
                                     (first-three double-cantus)
                                     (first-three counter)
                                     (rest-bars double-cantus)
                                     (rest-bars counter)))))

(defn undisguised-direct-motion-of-downbeats-to-perfect [[ca1 ca2 ca3] [cou1 cou2 cou3]]

  (if (or (rest? ca1) (rest? cou1))
    false
    (and (direct-motion-to-perfect? ca1 cou1 ca3 cou3)
         (not (and (= :contrary (type-of-motion ca2 cou2 ca3 cou3))
                   (> (Math/abs (get-interval (interval cou1 cou2))) 3))))))

(defn- no-undisguised-direct-motion-of-downbeats-to-perfect-iter?
  [cantus-bar counter-bar rest-cantus rest-counter]
  (and (rule-warning
        (not (undisguised-direct-motion-of-downbeats-to-perfect cantus-bar counter-bar))
        #(println "Undisguised direct motion to perfect interval: "
                  "\ncf" cantus-bar "\ncp" counter-bar))
       (if (< (count rest-cantus) 2)
         true
         (no-undisguised-direct-motion-of-downbeats-to-perfect-iter?
          (first-three rest-cantus)
          (first-three rest-counter)
          (rest-bars rest-cantus)
          (rest-bars rest-counter)))))

(defn no-undisguised-direct-motion-of-downbeats-to-perfect? [species]
  (let [double-cantus (remove-last (double-melody (get-cantus species)))
        counter (get-counter species)]
    (no-undisguised-direct-motion-of-downbeats-to-perfect-iter?
     (first-three double-cantus)
     (first-three counter)
     (rest-bars double-cantus)
     (rest-bars counter))))

(defn second-species-rules? [species]
  (and
   (rule-warning (= (inc (* 2 (dec (count (get-cantus species)))))
                    (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
  ;;  (last-interval? species)
  ;;  (ending? species)
   (allowed-melodic-intervals? species)
   (correct-downbeat-intervals species)
   (correct-upbeat-intervals species)
  ;;  (correct-intervals species)
  ;;  (no-direct-motion-to-perfect? species)
   (no-undisguised-direct-motion-of-downbeats-to-perfect? species)
;;    avoid consecutive perfect intervals
   ))

(defn evaluate-second-species [species]
  (let [;; harm-int (rest (remove-last (get-harmonic-intervals species)))
        ;; [p1-count p8-count p5-count] (map (fn [int]
        ;;                                     (count (filter #(= int %) harm-int)))
        ;;                                   [P1 P8 P5])
        ;; cp-ints (melodic-intervals (get-counter species))
        ;; ca-ints (melodic-intervals (get-cantus species))
        ;; simult-leaps (simultaneous-leaps ca-ints cp-ints)

        ;; score (+ (* -10 p1-count)
        ;;          (* -5 p8-count)
        ;;          (* -2 p5-count)
        ;;          (* -20 simult-leaps))
        score 0
        melody-s (melody-score (get-counter species))]
    (float (/ (+ score melody-s) (count (get-cantus species))))))
