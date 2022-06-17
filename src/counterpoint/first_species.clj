(ns counterpoint.first-species
  (:require [clojure.java.shell :refer [sh]]
            [counterpoint.core :refer [interval simple-interval]]
            [counterpoint.intervals :refer [get-interval get-quality m6
                                            make-interval P1 P8]]
            [counterpoint.melody :refer [last-interval make-melody
                                         melodic-intervals melody->lily]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [rule-warning]]))

(defn  make-first-species [cantus-firmus counterpoint-melody position]
  [cantus-firmus counterpoint-melody position])

(defn get-cantus [[c _ _]] c)
(defn get-counter [[_ m _]] m)
(defn get-position [[_ _ p]] p)

(defn- correct-interval [note1 note2]
  (let [harmony (simple-interval note1 note2)
        interval (Math/abs (get-interval harmony))]
    (case (get-quality harmony)
      :perfect (not= interval 4)
      :minor (or (= interval 3)
                 (= interval 6))
      :major (or (= interval 3)
                 (= interval 6))
      false)))

(defn- correct-intervals-iter [position note1 note2 notes1 notes2]
  (and (if (= position :above)
         (rule-warning (correct-interval note1 note2)
                       #(str "Not allowed harmonic interval " note1 note2 (interval note1 note2)))
         (rule-warning (correct-interval note2 note1)
                       #(str "Not allowed harmonic interval " note2 note1 (interval note2 note1))))
       (if (empty? notes1)
         true
         (correct-intervals-iter position (first notes1) (first notes2) (rest notes1) (rest notes2)))))

(defn correct-intervals [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (if (not= (count cantus) (count counter))
      false
      (correct-intervals-iter position (first cantus) (first counter) (rest cantus) (rest counter)))))

(defn last-interval? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        last (if (= position :above)
               (simple-interval (last cantus) (last counter))
               (simple-interval (last counter) (last cantus)))]
    (rule-warning (or (= last P1) (= last P8)) #(str "Last interval not a P1 or P8 " last))))

(defn ending? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        last-cantus (last-interval cantus)
        last-counter (last-interval counter)]
    (rule-warning (or (and (= last-cantus (make-interval -2 :major))
                           (= last-counter (make-interval 2 :minor)))
                      (and (= last-cantus (make-interval -2 :minor))
                           (= last-counter (make-interval 2 :major)))
                      (and (= last-cantus (make-interval 2 :major))
                           (= last-counter (make-interval -2 :minor)))
                      (and (= last-cantus (make-interval 2 :minor))
                           (= last-counter (make-interval -2 :major))))
                  #(str "Does not approach the ending by contrary motion and leading tone " last-cantus last-counter))))

(defn- no-direct-motion? [n1 n2 next1 next2]
  (let [interval1 (get-interval (simple-interval n1 next1))
        interval2 (get-interval (simple-interval n2 next2))]
    (rule-warning (or (= interval1 1)
                      (= interval2 1)
                      (neg? (* interval1 interval2))) #(str "Direct motion from " n1 n2 " to " next1 next2 " intervals: " (interval n1 n2) (interval next1 next2)))))

(defn- no-direct-motion-to-perfect-iter [position n1 n2 notes1 notes2]
  (if (empty? notes1)
    true
    (let [next1 (first notes1)
          next2 (first notes2)
          next-interval (interval next1 next2)]
      (and (or (not= (get-quality next-interval) :perfect)
               (if (= position :above)
                 (no-direct-motion? n1 n2 next1 next2)
                 (no-direct-motion? n2 n1 next2 next1)))
           (no-direct-motion-to-perfect-iter position next1 next2 (rest notes1) (rest notes2))))))


(defn no-direct-motion-to-perfect? [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (no-direct-motion-to-perfect-iter position (first cantus) (first counter) (rest cantus) (rest counter))))

(defn- allowed-melodic-interval [interval]
  (rule-warning
   (or (<= (Math/abs (get-interval interval)) 5)
       (= interval m6)
       (= interval P8))
   #(str "Not allowed interval in melody: " interval)))

(Math/abs 3)

(defn allowed-melodic-intervals? [counter-intervals]
  (if (empty? counter-intervals)
    true
    (and (allowed-melodic-interval (first counter-intervals))
         (allowed-melodic-intervals? (rest counter-intervals)))))

(defn first-species-rules? [species]
  (and
   (rule-warning (= (count (get-cantus species)) (count (get-counter species)))
                 #(str "Cantus and counterpoint have different number of notes"))
   (last-interval? species)
   (ending? species)
   (correct-intervals species)
   (no-direct-motion-to-perfect? species)
   (allowed-melodic-intervals? (melodic-intervals (get-counter species)))
;;    avoid consecutive perfect intervals
   ))

(defn first-species->lily [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (spit "resources/temp.ly"
          (str "\\version \"2.22.2\"
\\language \"english\"
\\score {
  \\new Staff <<
  \\set Staff.midiInstrument = #\"choir aahs\"
  \\new Voice = \"first\"
    \\relative c { \\voiceOne "
               (if (= position :above)
                 (melody->lily counter)
                 (melody->lily cantus))

               "}
  \\new Voice= \"second\"
    \\relative c { \\voiceTwo "
               (if (= position :above)
                 (melody->lily cantus)
                 (melody->lily counter))

               "}

>>
  \\layout { }
  \\midi { }
}
"))
    (sh "lilypond" "-o" "resources" "resources/temp.ly")))

(comment
  (def species (let [counterpoint-melody
                     (make-melody n/d3 n/d3 n/a4 n/f3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
                     cantus-firmus
                     (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
                 (make-first-species cantus-firmus counterpoint-melody :below)))

  (def species (let [counterpoint-melody
                     (make-melody n/a4 n/a4 n/g3 n/a4 n/b4 n/c4 n/c4 n/b4 n/d4 n/c#4 n/d4)
                     cantus-firmus
                     (make-melody n/d3 n/f3 n/e3 n/d3 n/g3 n/f3 n/a4 n/g3 n/f3 n/e3 n/d3)]
                 (make-first-species cantus-firmus counterpoint-melody :above)))

  (def species
    (let [counterpoint-melody
          (make-melody n/e4 n/f4 n/g4 n/a5 n/e4 n/f4 n/f4 n/d4 n/e4)
          _ (print (melodic-intervals counterpoint-melody))
          cantus-firmus
          (make-melody n/e3 n/d3 n/e3 n/f3 n/g3 n/a4 n/d3 n/f3 n/e3)]
      (make-first-species cantus-firmus counterpoint-melody :above)))



  (first-species-rules? species)

  (first-species->lily species)

  (sh "timidity" "resources/temp.midi")

  (print (first-species->lily species))

  ;
  )