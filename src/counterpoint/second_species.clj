(ns counterpoint.second-species 
  (:require [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.lilypond :refer [second-species->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(defn make-second-species [cantus-firmus counterpoint-melody arg3]
  (make-first-species cantus-firmus counterpoint-melody arg3)
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


(def species (let [counterpoint-melody
                   (make-melody n/c4 n/b4 n/a4 n/b4 
                                n/c4 n/d4 n/e4 n/d4 n/g3 n/b4 n/c4)
                   cantus-firmus ;; salieri
                   (make-melody n/c3 n/f3 n/e3 n/a4 n/g3 n/f3 n/e3 n/d3 n/c3)]
               (make-second-species cantus-firmus counterpoint-melody :above)))

species


(second-species->lily species)