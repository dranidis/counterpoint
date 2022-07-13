(ns counterpoint.gen-first-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus-firmi-examples :refer [salieri-d]]
            [counterpoint.core :refer [interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.first-species :refer [evaluate-species
                                                first-species-rules?]]
            [counterpoint.first-species-type :refer [make-first-species]]
            [counterpoint.generate-first-species :refer [next-reverse-candidates update-m36-size]]
            [counterpoint.intervals :refer [get-interval m10 m10- m2 m3 m3- M6
                                            M6- note-at-melodic-interval P1
                                            P8 P8-]]
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

(defn last-note-candidates [position cantus-note]
  (if (= position :above)
    [(note-at-melodic-interval cantus-note P8)
     (note-at-melodic-interval cantus-note P1)]
    [(note-at-melodic-interval cantus-note P8-)
     (note-at-melodic-interval cantus-note P1)]))

(defn second-to-last-note [position previous-melody previous-cantus cantus-note]
  (let [intval (if (= position :above)
                 (if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
                   (if (= m2 (interval cantus-note previous-cantus))
                     m10
                     M6)
                   (if (= m2 (interval cantus-note previous-cantus))
                     m3
                     m3- ;; crossing 
                     ))
                 (if (= (Math/abs (get-interval (interval previous-cantus previous-melody))) 8)
                   (if (= m2 (interval cantus-note previous-cantus))
                     M6-
                     m10-)
                   (if (= m2 (interval cantus-note previous-cantus))
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

(defn candidates [[position
                   key
                   melody
                   m36s ;; counter of thirds & sixths
                   previous-melody
                   previous-cantus
                   cantus-note
                   cantus-notes]]
  (case (count melody)
    0 (last-note-candidates-new position (first cantus-notes) cantus-note)
    1 (second-to-last-note-candidates position cantus-note previous-cantus)
    ;; [(second-to-last-note position previous-melody previous-cantus cantus-note)]
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
  ;; (println "NEXT mel" melody current)
  [position
   key
   (into melody [current])
   (update-m36-size m36s position cantus-note current)
   current
   cantus-note
   (first cantus-notes)
   (rest cantus-notes)])

(defn generate-reverse-counterpoint-dfs [position key cantus]
  (let [rev-cantus (reverse cantus)
        m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0 :remaining-cantus-size (count rev-cantus)}
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

(defn dfs-solution->cp [solution]
  (into [] (reverse (nth solution 2))))

;; (def cps (generate-reverse-random-counterpoint-dfs :above :c fux-d))
;; (take 1 cps)

(defn play [n cf key position]
  (let [cps (generate-reverse-counterpoint-dfs position key cf)
        cp (dfs-solution->cp (nth cps n))
        species (make-first-species cf cp position)
        ;; _ (println cp)
        ;; _ (println "RULES " (first-species-rules? species))
        ;; _ (println "EVAL  " (evaluate-species species))
        ]
    (species->lily species
                   {:clef (if (= position :above)
                            "treble"
                            "treble_8")
                    :pattern ""
                    :tempo "4 = 240"}))
  (sh/sh "timidity" "resources/temp.midi")
  ;; (sh/sh "timidity" "resources/temp.mid")
  )

;; (count (generate-reverse-counterpoint-dfs :above :c test-cf2))
;; (play 14 test-cf2 :c :above)

(defn play-best [cf key position]
  (let [cps (generate-reverse-counterpoint-dfs position key cf)
        _ (println "ALL" (count cps))
        species (apply max-key #(let [e (evaluate-species  %)]
                                ;; (println e)
                                  e)
                       (map #(make-first-species cf (dfs-solution->cp %) position) cps))
        _ (println "RULES " (first-species-rules? species))
        _ (println "EVAL  " (evaluate-species species))]
    (species->lily species
                   {:clef
                    (if (= position :above)
                      "treble"
                      "treble_8")
                    :pattern ""
                    :tempo "2 = 140"})
    (sh/sh "timidity" "resources/temp.midi")
  ;
    ))

;; (play-best fux-d :c :below)

;; (play-best mozart-c1 :c :below)
;; (play-best salieri-d :c :below)
;; (play-best salieri-c :c :above)
;; (play-best test-cf3 :c :below)
;; (play-best fux-e :c :above)

;; (play-best fux-e :c :above)

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