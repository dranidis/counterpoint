(ns counterpoint.gen-second-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.gen-first-dfs :refer [last-note-candidates
                                                second-to-last-note]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-second-species :refer [next-reverse-candidates-2nd]]
            [counterpoint.intervals :refer [get-interval m2 m2- M3- m6 m6-
                                            note-at-melodic-interval P12- P5 P5-]]
            [counterpoint.second-species :refer [evaluate-second-species
                                                 make-second-species
                                                 second-species-rules?]]))

(defn- third-to-last-note [position previous-melody previous-cantus cantus-note]
  (let [last-melody-m2? (= m2 (interval cantus-note previous-cantus))
        last-harmony-octave? (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
        intval (if (= position :above)
                 (if last-harmony-octave?
                   (if last-melody-m2?
                     m6
                     P5)
                   (if last-melody-m2?
                     m6
                     P5- ;; crossing 
                     ))
                 (if last-harmony-octave?
                   (if last-melody-m2?
                     M3-
                     P12-)
                   (if last-melody-m2?
                     m6 ;; crossing
                     (if (= m2- (interval cantus-note previous-cantus))
                       m6- ;; fa-mi
                       P5-))))]
    (note-at-melodic-interval cantus-note intval)))

(defn second-to-last-measure-candidates-2nd [position previous-melody previous-cantus cantus-note]
  [[(second-to-last-note position previous-melody previous-cantus cantus-note)
    (third-to-last-note position previous-melody previous-cantus cantus-note)]])

(defn candidates [{:keys [position
                          key
                          melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]
                   :as state}]
  (case (count melody)
    0 (map (fn [n] [n]) 
           (last-note-candidates position (first cantus-notes) cantus-note))
    1 (second-to-last-measure-candidates-2nd position previous-melody previous-cantus cantus-note)
    (next-reverse-candidates-2nd state)))

(def generate-second
  (generate-template
   candidates
   evaluate-second-species
   make-second-species
   second-species-rules?))

(defn play-best-second [n cf position]
  (let [sp (generate-second n cf position
                            {:pattern ""
                             :midi "flute"})]
    (sh/sh "timidity" "resources/temp.midi")
    sp))





(comment
  (play-best-second 100 fux-d :above)
  (def sp (generate-second 1 fux-d :above
                           {:pattern ""
                            :midi "flute"}))


  ;
  )