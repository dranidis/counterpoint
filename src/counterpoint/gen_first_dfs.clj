(ns counterpoint.gen-first-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [fux-a fux-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.first-species :refer [evaluate-species
                                                first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval m10- m3- M6
                                            note-at-melodic-interval P1 P8 P8-]]
            [counterpoint.lilypond :refer [species->lily]]))

(defn solution? [[position
                  key
                  melody
                  m36s ;; counter of thirds & sixths
                  previous-melody
                  previous-cantus
                  cantus-note
                  cantus-notes]]
  (= (get m36s :remaining-cantus-size) 0))

(defn candidates [[position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   cantus-note
                   cantus-notes]]
  (case (count melody)
    0 (if (= position :above)
        [(note-at-melodic-interval cantus-note P8)]
        [(note-at-melodic-interval cantus-note P8-)
         (note-at-melodic-interval cantus-note P1)])
    1 (if (= position :above)
        [(note-at-melodic-interval cantus-note M6)]
        [(if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
           m10- 
           m3-)])
    (next-reverse-candidates
     position key melody m36s previous-melody previous-cantus cantus-note)))

(defn next-node [[position
                  key
                  melody
                  m36s ;; counter of thirds & sixths
                  previous-melody
                  previous-cantus
                  cantus-note
                  cantus-notes]
                 current]
  (println melody current)
  [position
   key
   (into melody [current])
   (update-m36-size m36s position cantus-note current)
   current
   cantus-note
   (first cantus-notes)
   (rest cantus-notes)])

(defn generate-reverse-random-counterpoint-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (dec (count rev-cantus))}
        melody []
        previous-melody nil
        previous-cantus nil
        root-node [position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   (first rev-cantus)
                   (rest rev-cantus)]]
    (generate-dfs-solutions root-node candidates next-node solution?)))



(def cps (generate-reverse-random-counterpoint-dfs :above :c fux-a))

(defn play [n cf position]
  (let [cp (reverse (nth (nth cps n) 2))
        species (make-first-species cf cp position)
        ;; _ (println cp)
        _ (println "RULES " (first-species-rules? species))
        _ (println "EVAL  " (evaluate-species species))]
    (species->lily species
                   {:clef (if (= position :above)
                            "treble"
                            "treble_8")
                    :pattern ""
                    :tempo "4 = 240"}))
  (sh/sh "timidity" "resources/temp.midi")
  ;; (sh/sh "timidity" "resources/temp.mid")
  )

(play 0 fux-d :above)

;; (apply max (doall (map #(evaluate-species (make-first-species fux-d (reverse (nth % 2)) :above)) cps)))


(let [cf fux-d
      key :c
      position :below
      cps (generate-reverse-random-counterpoint-dfs position key cf)
      _ (println "ALL" (count cps))


      species (apply max-key #(let [e (evaluate-species  %)]
                                (println e)
                                e)
                     (map #(make-first-species cf (reverse (nth % 2)) position) cps))
      _ (println "RULES " (first-species-rules? species))
      _ (println "EVAL  " (evaluate-species species))]
  (species->lily species
                 {:clef
                  (if (= position :above)
                    "treble"
                    "treble_8")
                  :pattern ""
                  :tempo "4 = 240"})
  (sh/sh "timidity" "resources/temp.midi")
  ;
  )

;; (def position :below)
;; (def key :c)
;; (def cf fux-d)
;; (def cps (generate-reverse-random-counterpoint-dfs position key cf))
;; (def map-cps (map-indexed #(vector %1 %2) cps))

;; (map (fn [[n s]]
;;        (species->lily (make-first-species cf (reverse (nth s 2)) position) 
;;                      {:clef
;;                       (if (= position :above)
;;                         "treble"
;;                         "treble_8")
;;                       :pattern ""
;;                       :tempo "4 = 240"
;;                       :file (str "temp_" n)}))
;;      map-cps)

;; (map-indexed #(vector %1 %2) cps)