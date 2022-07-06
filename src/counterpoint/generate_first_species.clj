(ns counterpoint.generate-first-species
  (:require [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.core :refer [interval]]
            [counterpoint.intervals :refer [get-interval m10- m2 M2 m2-
                                            M2- m3 M3 m3- M3- M6 m6-
                                            note-at-diatonic-interval note-at-melodic-interval
                                            get-quality
                                            P1 P4 P4- P5 P5- P8 P8-]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.motion :refer [reverse-direct-perfect?]]
            [counterpoint.notes :refer [get-nooctave] :as n]))

;;
;; GLOBAL generation parameters
;;
(def harmonic-repetition-limit 3)
(def max-harmonic-interval 10)
(def debug? false)


(defn melody-reverse-leap
  [melody]
  (let [i (get-interval (interval (nth melody (- (count melody) 2)) (last melody)))]
    ;; (println "Melody int" melody i)
    (cond
      (= (Math/abs i) 1) :unison
      (> i 3) :low
      (< i -3) :high
      (neg? i) :no-leap-high
      :else :no-leap-low)))


(defn next-melodic-intervals-reverse [melody]
  (case (melody-reverse-leap melody)
    :unison [m2 M2 m3 M3
             m2- M2- m3- M3-]
    :high [m2 M2 m3 M3]
    :low [m2- M2- m3- M3-]
    :no-leap-high [
                  ;;  P1
                   m2 M2 m3 M3
                   P4 P5 P8
                   m2- M2- m3- M3-]
    [
    ;;  P1
     m2 M2 m3 M3
     m2- M2- m3- M3-
     P4- P5- m6-
     P8-]))

(defn next-harmonic-intervals [position m36s]
  (map #(* % (if (= position :above) 1 -1))
       (cond (and (= position :below) (= (get m36s :remaining-cantus-size) 1))
             [1]
             (and (= position :above) (= (get m36s :remaining-cantus-size) 1))
             [1 3 5]
             :else
            ;;  [1 3 5 6]
             [3 5 6])))

(defn- species-interval [position next-cantus-note mc]
  (if (= position :above)
    (interval next-cantus-note mc)
    (interval mc next-cantus-note)))

(defn dim-or-aug-filter [position next-cantus-note]
  (fn [mc]
    (let [int-quality (get-quality
                       (species-interval position next-cantus-note mc))]
      (not (or (= :aug int-quality)
               (= :dim int-quality))))))

(defn consecutive-parallel-filter [position next-cantus-note m36s]
  (fn [mc]
    (let [int-distance (get-interval
                        (species-interval position next-cantus-note mc))
          harmonic-repetitions (case int-distance
                                 3 (get m36s :thirds)
                                 6 (get m36s :sixths)
                                 10 (get m36s :tens)
                                 13 (get m36s :thirteens)
                                 0)
                        ;;  _ (println "REP" int-distance harmonic-repetitions)
          ]
      (< harmonic-repetitions harmonic-repetition-limit))))

(defn crossing-filter 
  ([position next-cantus-note] (crossing-filter position next-cantus-note true))
  ([position next-cantus-note unison-allowed?]
  (if unison-allowed?
    (fn [mc] (pos? (get-interval (species-interval position next-cantus-note mc))))
    (fn [mc] (> (get-interval (species-interval position next-cantus-note mc)) 1)))))

(defn debug [s c]
  (when debug? (println s c)) c)

(defn next-reverse-candidates [position key melody m36s previous-melody previous-cantus cantus-note]
  (let [next-melodic-candidates (map
                                 #(note-at-melodic-interval previous-melody %)
                                 (next-melodic-intervals-reverse melody))
        next-harmonic-candidates (map
                                  #(note-at-diatonic-interval key (get-nooctave cantus-note) %)
                                  (next-harmonic-intervals position m36s))
        _ (debug "next-harmonic-candidates" next-harmonic-candidates)]
    (->> next-melodic-candidates
         (filter (dim-or-aug-filter position cantus-note))
         (filter #((set next-harmonic-candidates) (get-nooctave %)))
         (filter (consecutive-parallel-filter position cantus-note m36s))
         (debug "no rep")
         (filter (crossing-filter position cantus-note))
         (filter #(not (reverse-direct-perfect? previous-cantus previous-melody cantus-note %)))
         (debug "no direct perfect")
         (filter #(maximum-range-M10? (append-to-melody melody %)))
         (debug "mel range")
         (filter #(<= (Math/abs (get-interval (interval cantus-note %))) max-harmonic-interval))
         (debug "harm range"))))

(defn- update-m36-size [m36s position cantus-note counter-note]
  (update (case (get-interval (species-interval position cantus-note counter-note))
            3 (-> m36s (update :thirds inc) (assoc :sixths 0) (assoc :tens 0) (assoc :thirteens 0))
            6 (-> m36s (update :sixths inc) (assoc :thirds 0) (assoc :tens 0) (assoc :thirteens 0))
            10 (-> m36s (update :tens inc) (assoc :thirds 0) (assoc :sixths 0) (assoc :thirteens 0))
            13 (-> m36s (update :thirteens inc) (assoc :thirds 0) (assoc :tens 0) (assoc :sixths 0))
            (-> m36s (assoc :thirds 0) (assoc :sixths 0) (assoc :tens 0) (assoc :thirteens 0)))
          :remaining-cantus-size dec))

(defn- generate-reverse-random-counterpoint-iter
  [position key melody m36s previous-melody previous-cantus cantus-note cantus-notes]
  ;; (println (reverse melody))
  (let [candidates (next-reverse-candidates
                    position key melody m36s previous-melody previous-cantus cantus-note)
        ;; _ (println candidates)
        current (rand-nth candidates)]
    (into [current]
          (if (empty? cantus-notes)
            []
            (generate-reverse-random-counterpoint-iter
             position
             key
             (into melody [current])
             (update-m36-size m36s position cantus-note current)
             current cantus-note (first cantus-notes) (rest cantus-notes))))))

(defn ending-first [position rev-cantus]
  (let [octave? (> (rand-int 10) 4)
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

(last [1 2])
(defn generate-reverse-random-counterpoint [position key cantus]
  (try
    (let [rev-cantus (reverse cantus)
          [last-melody second-last-melody m36s] (ending-first position rev-cantus)
          melody [last-melody second-last-melody]]
      (into
       (into []
             (reverse
              (generate-reverse-random-counterpoint-iter
               position
               key
               melody
               m36s ;; counter of thirds & sixths
               second-last-melody
               (second rev-cantus)
               (nth rev-cantus 2)
               (subvec (into [] rev-cantus) 3))))
       [second-last-melody last-melody]))
    (catch Exception _
      (println "Trying again...")
      (generate-reverse-random-counterpoint position key cantus))))
