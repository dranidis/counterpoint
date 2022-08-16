(ns counterpoint.gen-first-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.first-species :refer [evaluate-species-first
                                                first-species-rules? make-first-species]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-first-species :refer [next-reverse-candidates-1st]]
            [counterpoint.intervals :refer [get-interval m10 m10- m2 m3 m3- M6
                                            M6- note-at-melodic-interval P1
                                            P8 P8-]]))

(defn second-to-last-note [position previous-melody previous-cantus cantus-note]
  (let [last-harmony-octave? (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
        last-melody-m2? (= m2 (interval cantus-note previous-cantus))
        intval (if (= position :above)
                 (if last-harmony-octave?
                   (if last-melody-m2?
                     m10
                     M6)
                   (if last-melody-m2?
                     m3
                     m3- ;; crossing 
                     ))
                 (if last-harmony-octave?
                   (if last-melody-m2?
                     M6-
                     m10-)
                   (if last-melody-m2?
                     m3 ;; crossing
                     m3-)))]
    (note-at-melodic-interval cantus-note intval)))

(defn last-note-candidates [position cantus-sec-to-last cantus-last]
  (let [descending-cantus-end? (< (get-interval (interval cantus-sec-to-last cantus-last)) 0)]
    (map #(note-at-melodic-interval cantus-last %)
       (if descending-cantus-end?
         (if (= position :above)
           [P8]
           [P1 P8-])
         (if (= position :above)
           [P1 P8]
           [P8-])))))

(defn- second-to-last-note-candidates [position cantus-sec-to-last cantus-last]
  (let [descending-cantus-end? (< (get-interval (interval cantus-sec-to-last cantus-last)) 0)] 
    (if descending-cantus-end?
    (if (= position :above)
      [(note-at-melodic-interval cantus-sec-to-last M6)]
      [(note-at-melodic-interval cantus-sec-to-last m3-)
       (note-at-melodic-interval cantus-sec-to-last m10-)])
    (if (= position :above)
      [(note-at-melodic-interval cantus-sec-to-last m3)
       (note-at-melodic-interval cantus-sec-to-last m10-)]
      [(note-at-melodic-interval cantus-sec-to-last M6-)]))))

(defn candidates [{:keys [position
                          key
                          melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]
                   :as state}]
  (let [cand (case (count melody)
               0 (last-note-candidates position (first cantus-notes) cantus-note)
               1 (second-to-last-note-candidates position cantus-note previous-cantus)
    ;; [(second-to-last-note position previous-melody previous-cantus cantus-note)]
               (next-reverse-candidates-1st state))]
    (map (fn [c] [c]) cand)))

(def generate-first
  (generate-template
   candidates
   evaluate-species-first
   make-first-species
   first-species-rules?))

(defn play-best-first [n cf position]
  (generate-first n cf position
                  {:pattern ""
                   :midi "flute"})
  (sh/sh "timidity" "resources/temp.midi"))

(comment
  (play-best-first 100 fux-d :below)
  ;
  )