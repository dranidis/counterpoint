(ns counterpoint.gen-fourth-dfs
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [maximum-range-M10?]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.dfs.dfs :refer [generate-dfs-solutions]]
            [counterpoint.fourth-species :refer [evaluate-fourth-species
                                                 fourth-species-rules?
                                                 make-fourth-species]]
            [counterpoint.gen-first-dfs :refer [last-note-candidates next-node
                                                second-to-last-note solution?]]
            [counterpoint.gen-second-dfs :refer [second-to-last-measure-candidates-2nd]]
            [counterpoint.generate :refer [generate-template]]
            [counterpoint.generate-first-species :refer [crossing-filter debug
                                                         dim-or-aug-filter
                                                         max-harmonic-interval next-harmonic-intervals]]
            [counterpoint.generate-second-species :refer [next-candidate-notes
                                                          next-reverse-downbeat-candidates]]
            [counterpoint.intervals :refer [get-interval harmonic-consonant?
                                            m2 M2 m7 M7 m9 M9 next-diatonic
                                            note-at-diatonic-interval note-at-melodic-interval P1 P8]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [append-to-melody]]
            [counterpoint.notes :refer [get-nooctave] :as n]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(defn second-to-last-measure-candidates-4th
  [position key-sig previous-melody previous-cantus cantus-note next-cantus]
  ;; (println "Next cantus" next-cantus)
  (let [sec-to-last-note (second-to-last-note position
                                              previous-melody
                                              previous-cantus
                                              cantus-note)
        third-to-last-note (next-diatonic key-sig sec-to-last-note)
        can-be-a-suspension? (harmonic-consonant?
                              (simple-interval next-cantus third-to-last-note position))
        sec-species-measure (second-to-last-measure-candidates-2nd
                             position previous-melody previous-cantus cantus-note)]
    (if can-be-a-suspension?
      (into [[sec-to-last-note
              third-to-last-note]] sec-species-measure)
      sec-species-measure)))

(defn- one-two-suspension? [next-cantus cantus-note suspension-note]
  (let [preparation-interval (interval next-cantus suspension-note)
        suspension-interval (interval cantus-note suspension-note)]
    (or (and (= P1 preparation-interval)
             (or (= m2 suspension-interval)
                 (= M2 suspension-interval)))
        (and (= P8 preparation-interval)
             (or (= m9 suspension-interval)
                 (= M9 suspension-interval))))))

(defn- eight-seven-suspension? [next-cantus cantus-note suspension-note]
  (let [preparation-interval (interval suspension-note next-cantus)
        suspension-interval (interval suspension-note cantus-note)]
    (and (= P8 preparation-interval)
         (or (= m7 suspension-interval)
             (= M7 suspension-interval)))))

(defn- not-allowed-suspension [position next-cantus cantus-note suspension-note]
  (if (= position :above)
    (one-two-suspension? next-cantus cantus-note suspension-note)
    (eight-seven-suspension? next-cantus cantus-note suspension-note)))

(comment
  (one-two-suspension? n/g3 n/f3 n/g3)
  (one-two-suspension? n/g3 n/f3 n/g4)
  ;
  )

(defn- suspension-notes [position key melody m36s upbeat-note cantus-note next-cantus]
  ;; (println "SUSPENSION-NOTES melody" melody)
  (if (nil? next-cantus)
    []
    (let [next-melodic-candidates (map
                                   #(note-at-melodic-interval upbeat-note %)
                                   [m2 M2])
          next-harmonic-candidates (map
                                    #(note-at-diatonic-interval key (get-nooctave next-cantus) %)
                                    (next-harmonic-intervals
                                     position
                                     ;; for the previous measure
                                     (update m36s :remaining-cantus-size dec)))
          suspension-notes (->> next-melodic-candidates
                                (filter (dim-or-aug-filter position next-cantus))
                                (debug "melodic m2 M2")
                                (filter #((set next-harmonic-candidates) (get-nooctave %)))
                                (debug "harmonic with previous")
                                (filter #(not (harmonic-consonant? (simple-interval cantus-note % position))))
                                (debug "dissonant with current")
                                (filter #(not (not-allowed-suspension position next-cantus cantus-note %)))
                                (debug "no 1-2 and 8-9 suspension (8-7 below)")
                                (filter (crossing-filter position next-cantus))
                                (debug "no crossing")
                                (filter #(maximum-range-M10? (append-to-melody melody [%])))
                                (filter #(<= (Math/abs (get-interval (interval next-cantus %))) max-harmonic-interval)))]
      suspension-notes)))

(defn next-reverse-candidates-4th [{:keys [position key melody m36s
                                           previous-melody previous-cantus cantus-note cantus-notes]}]
  (let [next-cantus (first cantus-notes)
        was-a-suspension? (not (harmonic-consonant? (simple-interval previous-cantus previous-melody position)))
        ;; _ (when was-a-suspension? (println "SUSP"  previous-melody))
        upbeat-candidates
        (if was-a-suspension?
          [previous-melody]
          (next-candidate-notes
           position key melody m36s
           previous-melody previous-cantus cantus-note
           {:melodic-unison-allowed true}))
        ;; _ (println "UPC" upbeat-candidates)
        candidates
        (reduce
         #(into %1 %2) []
         (map (fn [upc]
                (for [downbeat
                      (into
                       (next-reverse-downbeat-candidates position
                                                         key
                                                         (into  melody [upc])
                                                         m36s
                                                         upc
                                                          ;; same note in cantus (held)
                                                         cantus-note
                                                         cantus-note)
                       (suspension-notes position
                                         key (into  melody [upc]) m36s upc cantus-note next-cantus))
                      upbeat [upc]]
                  (vector upbeat downbeat)))
              upbeat-candidates))]
    ;; (println "Candidates" candidates)
    (->> (remove nil?
                ;;  (into 
                 candidates
                  ;; passing)
                 )
        ;;  (filter (fn [[upbeat downbeat]]
        ;;            (let [cantus-bar [cantus-note cantus-note previous-cantus]
        ;;                  counter-bar [downbeat upbeat previous-melody]]
        ;;              (and
        ;;               (not (undisguised-direct-motion-of-downbeats-to-perfect cantus-bar counter-bar))
        ;;               (not (direct-motion-to-perfect? cantus-note upbeat previous-cantus previous-melody))))))
         )))

(defn candidates [{:keys [position
                          key
                          melody
                          m36s ;; counter of thirds & sixths
                          previous-melody
                          previous-cantus
                          cantus-note
                          cantus-notes]
                   :as state}]
  ;; (println "cantus-notes" cantus-notes)
  ;; (println melody (count melody))
  (let [cand (case (count melody)
               0 (map (fn [n] [n]) (last-note-candidates position cantus-note))
               1 (second-to-last-measure-candidates-4th
                  position key previous-melody previous-cantus cantus-note (first cantus-notes))
               (next-reverse-candidates-4th state))]
    ;; (when (and (empty? cand) (not (solution?
    ;;                                [position
    ;;                                 key
    ;;                                 melody
    ;;                                 m36s ;; counter of thirds & sixths
    ;;                                 previous-melody
    ;;                                 previous-cantus
    ;;                                 cantus-note
    ;;                                 cantus-notes])))
    ;;   ;; (println "DEADEND:" melody)
    ;;   ;; (melody->lily (reverse melody) {:file "resources/temp1.ly"})
    ;;   )
    ;; (println "CAND" cand)
    cand))

(defn generate-reverse-counterpoint-4th-dfs [position key cantus]
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

(def generate-fourth
  (generate-template
   generate-reverse-counterpoint-4th-dfs
   evaluate-fourth-species
   make-fourth-species
   fourth-species-rules?))

(defn play-best-fourth [n cf position]
  (generate-fourth n cf position
                   {:pattern ""
                    :midi "acoustic grand"})
  (sh/sh "timidity" "resources/temp.midi"))

(comment
  (play-best-fourth 100 fux-d :above)

  (def cps (generate-reverse-counterpoint-4th-dfs :below :f fux-d))
  (def a-sol (make-fourth-species fux-d (dfs-solution->cp (nth cps 0)) :below))
  (evaluate-fourth-species a-sol {:verbose false})
  (species->lily a-sol {:clef "treble_8"
                        :pattern ""
                        :tempo "4 = 200"
                        :midi "acoustic grand"})
  (sh/sh "timidity" "resources/temp.midi")

  [:rest [:d 2 :natural] [:d 3 :natural] [:b 3 :flat] [:b 3 :flat] [:a 3 :natural] [:a 3 :natural] [:g 2 :natural] [:g 2 :natural] [:b 3 :flat] [:b 3 :flat] [:a 3 :natural] [:a 3 :natural] [:f 2 :natural] [:f 2 :natural] [:e 2 :natural] [:e 2 :natural] [:d 2 :natural] [:d 2 :natural] [:c 2 :sharp] [:d 2 :natural]]
  [[:d 3 :natural] [:f 3 :natural] [:e 3 :natural] [:d 3 :natural] [:g 3 :natural] [:f 3 :natural] [:a 4 :natural] [:g 3 :natural] [:f 3 :natural] [:e 3 :natural] [:d 3 :natural]]
; 

;
  )
