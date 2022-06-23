(ns counterpoint.second-species 
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [haydn]]
            [counterpoint.first-species :refer [allowed-melodic-intervals?
                                                correct-intervals-iter]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position
                                                     make-first-species]]
            [counterpoint.lilypond :refer [second-species->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [rule-warning]]))

(defn make-second-species [cantus-firmus counterpoint-melody arg3]
  (make-first-species cantus-firmus counterpoint-melody arg3)
  )

(defn- downbeats-iter [downbeats-melody rest]
  (if (empty? rest)
    downbeats-melody
    (let [next-downbeat (second rest)
          rest-melody (subvec (into [] rest) 2)]
      (downbeats-iter (into downbeats-melody [next-downbeat]) rest-melody)
      ))
  )

(defn downbeats-second [melody]
  (downbeats-iter [(first melody)] (rest melody))
  )

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
        _ (println counter)
        position (get-position species)]
    (if (not= (count cantus) (count counter))
      false
      (correct-intervals-iter position (first cantus) (first counter) (rest cantus) (rest counter)))))

(defn second-species-rules? [species]
  (and
   (rule-warning (= (inc (* 2 (dec (count (get-cantus species))))) 
                    (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
  ;;  (last-interval? species)
  ;;  (ending? species)
   (allowed-melodic-intervals? species)
   (correct-downbeat-intervals species)
  ;;  (correct-intervals species)
  ;;  (no-direct-motion-to-perfect? species)
;;    avoid consecutive perfect intervals
   ))


(comment
  
  
(def species (let [counterpoint-melody
                   (make-melody n/c4 n/b4 n/a4 n/b4
                                n/c4 n/g4 n/e4 n/c4
                                n/b4 n/d4 n/a4 n/b4
                                n/c4 n/g3 n/a4 n/b4
                                n/c4)
                   cantus-firmus ;; salieri
                   (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3)]
               (make-second-species cantus-firmus counterpoint-melody :above)))
(second-species->lily species)

(count haydn)

(def species (let [counterpoint-melody
      (make-melody n/d4 n/a4 
                   n/b4 n/c4
                   n/d4 n/e4 
                   n/f4 n/d4
                   n/c4 n/e4 
                   n/f4 n/d4
                   n/c4 n/g3 
                   n/b4 n/c4
                   n/d4 n/a4 
                   n/b4 n/c#4
                   n/d4)]
  (make-second-species haydn counterpoint-melody :above)))
(second-species-rules? species)
(second-species->lily species)

(sh/sh "timidity" "resources/temp.midi") 
;
)