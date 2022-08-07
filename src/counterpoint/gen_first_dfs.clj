(ns counterpoint.gen-first-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.first-species :refer [evaluate-species
                                                first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-first-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval m10 m10- m2 m3 m3- M6
                                            M6- note-at-melodic-interval P1
                                            P8 P8-]]))

(defn solution? [{:keys [position
                         key
                         melody
                         m36s ;; counter of thirds & sixths
                         previous-melody
                         previous-cantus
                         cantus-note
                         cantus-notes]}]
  (= (get m36s :remaining-cantus-size) 0))

(defn last-note-candidates [position cantus-note]
  (if (= position :above)
    [(note-at-melodic-interval cantus-note P8)
     (note-at-melodic-interval cantus-note P1)]
    [(note-at-melodic-interval cantus-note P1)
     (note-at-melodic-interval cantus-note P8-)]))

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

(defn- last-note-candidates-new [position cantus-sec-to-last cantus-last]
  (map #(note-at-melodic-interval cantus-last %)
       (if (< (get-interval (interval cantus-sec-to-last cantus-last)) 0)
         (if (= position :above)
           [P8]
           [P1 P8-])
         (if (= position :above)
           [P1 P8]
           [P8-]))))

(defn- second-to-last-note-candidates [position cantus-sec-to-last cantus-last]
  (if (< (get-interval (interval cantus-sec-to-last cantus-last)) 0)
    (if (= position :above)
      [(note-at-melodic-interval cantus-sec-to-last M6)]
      [(note-at-melodic-interval cantus-sec-to-last m3-)
       (note-at-melodic-interval cantus-sec-to-last m10-)])
    (if (= position :above)
      [(note-at-melodic-interval cantus-sec-to-last m3)
       (note-at-melodic-interval cantus-sec-to-last m10-)]
      [(note-at-melodic-interval cantus-sec-to-last M6-)])))

(defn candidates [{:keys [position
                          key
                          melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]}]
  (case (count melody)
    0 (last-note-candidates-new position (first cantus-notes) cantus-note)
    1 (second-to-last-note-candidates position cantus-note previous-cantus)
    ;; [(second-to-last-note position previous-melody previous-cantus cantus-note)]
    (next-reverse-candidates
     position key melody m36s previous-melody previous-cantus cantus-note)))

(defn next-node [{:keys [position
                         key
                         melody
                         m36s ;; counter of thirds & sixths
                         previous-melody
                         previous-cantus
                         cantus-note
                         cantus-notes]}
                 current]
  ;; (println "NEXT mel" melody current)
  {:position position
   :key key
   :melody (into melody [current])
   :m36s (update-m36-size m36s position cantus-note current)
   :previous-melody current
   :previous-cantus cantus-note
   :cantus-note (first cantus-notes)
   :cantus-notes (rest cantus-notes)})

(defn generate-reverse-counterpoint-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
        melody []
        previous-melody nil
        previous-cantus nil
        root-node {:position position
                   :key key
                   :melody melody
                   :m36s m36s;; counter of thirds & sixths
                   :previous-melody previous-melody
                   :previous-cantus previous-cantus
                   :cantus-note (first rev-cantus)
                   :cantus-notes (rest rev-cantus)}]
    (generate-dfs-solutions root-node candidates next-node solution?)))

(def generate-first
  (generate-template
   generate-reverse-counterpoint-dfs
   evaluate-species
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