(ns counterpoint.lilypond 
  (:require [clojure.java.shell :as sh]
            [counterpoint.core :refer [interval]]
            [counterpoint.first-species-type :refer [get-cantus get-counter
                                                get-position]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.notes :refer [get-acc get-note get-octave]]))

(defn- single-note->lily [note]
  (str " "
       (name (get-note note))
       (case (get-acc note)
         :sharp "is"
         :flat "es"
         "")))

(defn- note->lily [note]
  (str (single-note->lily note)
       (case (get-octave note)
         0 ",,"
         1 ","
         2 ""
         3 "'"
         4 "''"
         5 "'''")))

(defn- note->lily-relative [note previous]
  (let [interval-from-previous (get-interval (interval previous note))]
    (str (single-note->lily note)
         (if (< (Math/abs interval-from-previous) 5)
           ""
           (if (pos? interval-from-previous) "'" ",")))))

(defn- to-lily-iter [previous note notes]

  (into [(if (nil? previous)
           (note->lily note)
           (note->lily-relative note previous))]
        (if (empty? notes)
          []
          (to-lily-iter note (first notes) (rest notes)))))

(defn melody->lily [[note & notes]]
  (apply str (to-lily-iter nil note notes)))

(defn first-species->lily [species]
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (spit "resources/temp.ly"
          (str
           "\\score {
  \\new Staff <<\n"

           "\\set Staff.midiInstrument = #\"voice oohs\"\n"

           "\\new Voice = \"first\"
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
    (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))
