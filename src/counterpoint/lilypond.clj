(ns counterpoint.lilypond
  (:require [clojure.java.shell :as sh]
            [counterpoint.core :refer [interval]]
            [counterpoint.figured-bass :refer [figured-bass-first
                                               figured-bass-fourth figured-bass-second]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.notes :refer [get-acc get-note get-octave]]
            [counterpoint.notes :as n]
            [counterpoint.rest :refer [rest?]]))

;;
;; GLOBAL parameters
;;
(def midi-instrument "acoustic grand")


(defn- single-note->lily [note]
  (str " "
       (name (get-note note))
       (case (get-acc note)
         :sharp "is"
         :flat "es"
         "")))

(defn- note->lily [duration note]
  (str (if (rest? note)
         "r"
         (str (single-note->lily note)
              (case (+ (get-octave note)
                       (if (or (= (get-note note) :a)
                               (= (get-note note) :b))
                         -1 ;; lilypond starts from c0, we start from a0 
                         0))
                -1 ",,,"
                0 ",,"
                1 ","
                2 ""
                3 "'"
                4 "''"
                5 "'''") duration))))
(comment
  (note->lily 1 (n/make-note :a 0))
  (note->lily 1 (n/make-note :b 0))
  (note->lily 1 (n/make-note :c 0))
  ;
  )

(defn- note->lily-relative [note previous]
  (let [interval-from-previous (get-interval (interval previous note))]
    (str (single-note->lily note)
         (if (< (Math/abs interval-from-previous) 5)
           ""
           (if (pos? interval-from-previous) "'" ",")))))

(defn- relative-to-lily-iter [duration previous note notes]
  (into [(if (nil? previous)
           (note->lily duration note)
           (note->lily-relative note previous))]
        (if (empty? notes)
          []
          (relative-to-lily-iter duration note (first notes) (rest notes)))))

(defn- fixed-to-lily-fourth-iter [duration note notes]
  (if (empty? notes)
    [(note->lily (quot duration 2) note)]
    (let [lily (note->lily duration note)
          next (first notes)
          others (rest notes)]
      (if (= note next)
        (into [(str lily "~")  lily]
              (if (empty? others)
                []
                (fixed-to-lily-fourth-iter duration (first others) (rest others))))
        (into [lily]
              (fixed-to-lily-fourth-iter duration (first notes) (rest notes)))))))

(defn relative-melody->lily [duration [note & notes]]
  (apply str (relative-to-lily-iter duration nil note notes)))

(defn fixed-melody->lily [duration notes]
  (apply str
         (map #(note->lily duration %) notes)))

(defn fixed-melody-fourth->lily [duration [note & notes]]
  (str "r" duration (apply str (fixed-to-lily-fourth-iter duration note notes))))

(defn staff [clef tempo voices]
  (str
   "\\score {
  \\new Staff <<
            \\clef \"" clef "\"\n
            \\tempo " tempo "\n"
   "\\set Staff.midiInstrument = #\"" midi-instrument
   "\"\n"
   voices
   ">>
  \\layout { }
  \\midi { }
}
"))

(defn melody->lily
  ([melody] (melody->lily melody "treble"))
  ([melody clef]

   (spit "resources/temp.ly"
         (staff clef "2 = 120"
                (str "  \\new Voice = \"first\"
     { \\voiceOne "
                     (fixed-melody->lily 1 melody)
                     "}")))
   (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))

(defn voice [first voiceOne melody]
  (str "  \\new Voice = \"" first "\"
     { \\" voiceOne " "
       melody
       "}\n"))

(defn first-voices [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (str
     (voice "first" "voiceOne"
            (if (= position :above)
              (fixed-melody->lily 1 counter)
              (fixed-melody->lily 1 cantus)))
     (voice "second" "voiceTwo"
            (if (= position :above)
              (fixed-melody->lily 1 cantus)
              (fixed-melody->lily 1 counter)))
     (figured-bass-first species))))

(defn pattern [p]
  (let [duration (count p)]
    (if (#{1 2 4 8 16 32 64 128} duration)
      (fn [n1 n2] (let [s1 (note->lily duration n1)
                        s2 (note->lily duration n2)]
                    (apply str (map #(if (= \a %) s1 s2) p))))
      (throw (Exception. (str "pattern: " p " has size " duration ". Length must be power of 2"))))))

(defn first-voices-pattern [p species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (str
     (voice "first" "voiceOne"
            (if (= position :above)
              (apply
               str
               (map (pattern p) cantus counter))
              (apply
               str
               (map (pattern p) counter cantus))))
    ;;  (figured-bass-first species)
     )))

(defn first-species->lily
  ([species] (first-species->lily species 
                                  {:clef "treble"
                                   :pattern ""
                                   :tempo "2 = 80"}))
  ([species param]
   (spit "resources/temp.ly"
         (staff
          (get param :clef "treble")
          (get param :tempo "2 = 80")
          (let [p (get param :pattern "")]
            (if (= p "")
              (first-voices species)
              (first-voices-pattern p species)))))
   (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))

(defn fourth-species->lily
  ([species] (fourth-species->lily species "treble"))
  ([species clef]
   (let [cantus (get-cantus species)
         counter (get-counter species)
         position (get-position species)]
     (spit "resources/temp.ly"
           (staff
            clef
            "2 = 80"
            (str
             (voice "first" "voiceOne"
                    (if (= position :above)
                      (fixed-melody-fourth->lily 2 counter)
                      (fixed-melody->lily 1 cantus)))
             (voice "second" "voiceTwo"
                    (if (= position :above)
                      (fixed-melody->lily 1 cantus)
                      (fixed-melody-fourth->lily 2 counter)))
             (figured-bass-fourth species))))
     (sh/sh "lilypond" "-o" "resources" "resources/temp.ly"))))


(defn end-to-1 [melody]
  (str (subs melody 0 (dec (count melody))) "1"))

(defn second-species->lily [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (spit "resources/temp.ly"
          (str
           "\\score {
  \\new Staff <<
           \\tempo 2 = 90
           \\set Staff.midiInstrument = #\"" midi-instrument "\"\n"

           "  \\new Voice = \"first\"
     { \\voiceOne "
           (if (= position :above)
             (end-to-1 (fixed-melody->lily 2 counter))
             (fixed-melody->lily 1 cantus))

           "}
  \\new Voice= \"second\"
     { \\voiceTwo "
           (if (= position :above)
             (fixed-melody->lily 1 cantus)
             (end-to-1 (fixed-melody->lily 2 counter)))
           "}
  \\figures {"
           (figured-bass-second species)


           "}
 >>
  \\layout { }
  \\midi { }
}
"))
    (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))




