(ns counterpoint.lilypond
  (:require [clojure.java.shell :as sh]
            [counterpoint.core :refer [interval]]
            [counterpoint.figured-bass :refer [figured-bass
                                               figured-bass-second]]
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

(defn- note->lily [duration note]
  (str (if (or (= (get-note note) :a)
          (= (get-note note) :b))
    (str (single-note->lily note)
         (case (get-octave note)
           0 ",,,"
           1 ",,"
           2 ","
           3 ""
           4 "'"
           5 "''"))
    (str (single-note->lily note)
         (case (get-octave note)
           0 ",,"
           1 ","
           2 ""
           3 "'"
           4 "''"
           5 "'''"))
    ) duration))

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

(defn- fixed-to-lily-iter [duration note notes]
  (into [(note->lily duration note)]
        (if (empty? notes)
          []
          (fixed-to-lily-iter duration (first notes) (rest notes)))))

(defn relative-melody->lily [duration [note & notes]]
  (apply str (relative-to-lily-iter duration nil note notes)))

(defn fixed-melody->lily [duration [note & notes]]
  (apply str (fixed-to-lily-iter duration note notes)))

(defn first-species->lily 
  ([species] (first-species->lily species "treble")) 
  ([species clef] 
  (let [cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)]
    (spit "resources/temp.ly"
          (str
           "\\score {
  \\new Staff <<
            \\clef \"" clef "\"\n
            \\tempo 2 = 120
            \\set Staff.midiInstrument = #\"voice oohs\"\n"

           "  \\new Voice = \"first\"
     { \\voiceOne "
           (if (= position :above)
             (fixed-melody->lily 1 counter)
             (fixed-melody->lily 1 cantus))

           "}
  \\new Voice= \"second\"
     { \\voiceTwo "
           (if (= position :above)
             (fixed-melody->lily 1 cantus)
             (fixed-melody->lily 1 counter))
           "}
  \\figures {"
           (figured-bass species)


           "}
 >>
  \\layout { }
  \\midi { }
}
"))
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
           \\tempo 2 = 70
           \\set Staff.midiInstrument = #\"voice oohs\"\n"

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


