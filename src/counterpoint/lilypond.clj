(ns counterpoint.lilypond
  (:require [clojure.java.shell :as sh]
            [counterpoint.core :refer [interval]]
            [counterpoint.figured-bass :refer [figured-bass-first
                                               figured-bass-fourth figured-bass-second]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                     get-position get-type]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.melody :refer [double-melody]]
            [counterpoint.notes :refer [get-acc get-note get-octave]]
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
                5 "'''")))
       duration))

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
  (apply str (map #(note->lily duration %) notes)))

(defn fixed-melody-fourth->lily [duration [note & notes]]
  (apply str (fixed-to-lily-fourth-iter duration note notes)))

(defn staff [clef tempo voices midi-instrument]
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
}"))

(defn melody->lily
  ([melody] (melody->lily melody "treble"))
  ([melody clef]
   (spit "resources/temp.ly"
         (staff clef "2 = 120"
                (str "  \\new Voice = \"first\"
     { \\voiceOne "
                     (fixed-melody->lily 1 melody)
                     "}")
                midi-instrument))
   (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))

(defn voice [first voiceOne melody]
  (str "  \\new Voice = \"" first "\"
     { \\" voiceOne " "
       melody
       "}\n"))

(defn end-to-1 [melody]
  (str (subs melody 0 (dec (count melody))) "1"))

(defn voices [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        type (get-type species)
        _ (println "TYPE " type)
        counter->lily (case type
                        :first (partial fixed-melody->lily 1)
                        :second (fn [counter]
                                  (end-to-1 (fixed-melody->lily 2 counter)))
                        :fourth (partial fixed-melody-fourth->lily 2))
        fig-bass (case type
                   :first figured-bass-first
                   :second figured-bass-second
                   :fourth figured-bass-fourth)]
    (str
     (voice "first" "voiceOne"
            (if (= position :above)
              (counter->lily counter)
              (fixed-melody->lily 1 cantus)))
     (voice "second" "voiceTwo"
            (if (= position :above)
              (fixed-melody->lily 1 cantus)
              (counter->lily counter)))
     (fig-bass species))))

(defn pattern
  ([p] (pattern p 1))
  ([p length]
   (let [duration (* (count p) length)]
     (if (#{1 2 4 8 16 32 64 128} duration)
       (fn [n1 n2] (let [s1 (note->lily duration n1)
                         s2 (note->lily duration n2)]
                     (apply str (map #(cond
                                        (= \a %) s1
                                        (= \r %) (str "r" duration)
                                        :else s2)
                                     p))))
       (throw (Exception. (str "pattern: " p " has size " duration ". Length must be power of 2")))))))

(defn voice-pattern [position p length cantus counter]
  (voice "first" "voiceOne"
         (if (= position :above)
           (apply
            str
            (map (pattern p length) cantus counter))
           (apply
            str
            (map (pattern p length) counter cantus)))))

(defn first-voices-pattern [p species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (str
     (voice-pattern position p 1 cantus counter)
     (figured-bass-first species))))

(defn second-voices-pattern [p species]
  (let [cantus (double-melody (get-cantus species))
        counter (get-counter species)
        position (get-position species)]
    (str
     (voice-pattern position p 2 cantus counter)
     (figured-bass-second species))))

(defn fourth-voices-pattern [p species]
  (let [cantus (double-melody (get-cantus species))
        counter (get-counter species)
        position (get-position species)]
    (str
     (voice-pattern position p 2 cantus counter)
     (figured-bass-fourth species))))

(defn voices-pattern [p species]
  (case (get-type species)
    :first (first-voices-pattern p species)
    :second (second-voices-pattern p species)
    :fourth (fourth-voices-pattern p species)
    (voices species)))

(defn species->lily
  ([species] (species->lily species
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
              (voices species)
              (voices-pattern p species)))
          (get param :midi midi-instrument)))
   (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))

