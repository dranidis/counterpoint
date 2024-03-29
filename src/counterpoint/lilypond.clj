(ns counterpoint.lilypond
  (:require [clojure.java.shell :as sh]
            [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.core :refer [interval]]
            [counterpoint.figured-bass :refer [figured-bass]]
            [counterpoint.species-type :refer [get-cantus get-counter
                                               get-position get-type]]
            [counterpoint.intervals :refer [get-interval]]
            [counterpoint.melody :refer [double-melody]]
            [counterpoint.notes :refer [get-acc get-note get-octave]]
            [counterpoint.rest :refer [rest?]]))

(defn- single-note->lily [note]
  (try (str " "
            (name (get-note note))
            (case (get-acc note)
              :sharp "is"
              :flat "es"
              ""))
       (catch Exception e
         (println "ERROR IN CALL: single-note->lily" note)
         (throw e))))

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
                5 "'''"
                6 "''''")))
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

(defn- fixed-to-lily-tied-iter [duration note notes]
  (if (empty? notes)
    [(note->lily duration note)]
    (let [lily (note->lily duration note)
          next (first notes)
          others (rest notes)]
      (if (= note next)
        (into [(str lily "~")  lily]
              (if (empty? others)
                []
                (fixed-to-lily-tied-iter duration (first others) (rest others))))
        (into [lily]
              (fixed-to-lily-tied-iter duration (first notes) (rest notes)))))))

(defn relative-melody->lily [duration [note & notes]]
  (apply str (relative-to-lily-iter duration nil note notes)))

(defn fixed-melody->lily [duration notes]
  (apply str (map #(note->lily duration %) notes)))

(defn fixed-melody->lily-tied [duration notes]
  (apply str (fixed-to-lily-tied-iter duration (first notes) (rest notes))))

(defn fixed-melody-fourth->lily [duration [note & notes]]
  (apply str (fixed-to-lily-fourth-iter duration note notes)))

(defn- sub-bar-fn [notes]
  (fixed-melody->lily
   (get notes :d)
   (get notes :n)))

(defn- bar-fn [bar]
  (str "~" (reduce #(str %1 (sub-bar-fn %2))
                   "" bar)))

(defn melody-fifth->lily [notes-with-duration]
  (try
    (apply str (mapv bar-fn notes-with-duration))
    (catch Exception e
      (println "ERROR IN CALL: melody-fifth->lily" notes-with-duration)
      (throw e))))

(defn- key->lily [key-sig]
  (case key-sig
    :bb "bes"
    :eb "ees"
    :ab "aes"
    :db "des"
    :gb "ges"
    :cb "ces"
    :f# "fis"
    :c# "cis"
    (name key-sig)))

(defn- key-signature->lily [key-signature]
  (str (key->lily key-signature) "\\major\n"))

(defn lilypond-score [voices]
  (str
   "\\score {
    <<
    "
   voices
   ">>
  \\layout { }
  \\midi { }
}"))

;; (defn melody->lily
;;   ([melody] (melody->lily melody "treble"))
;;   ([melody clef]
;;    (spit "resources/temp.ly"
;;          (staff clef "2 = 120"
;;                 (str "  \\new Voice = \"first\"
;;      { \\voiceOne "
;;                      (fixed-melody->lily 1 melody)
;;                      "}")
;;                 midi-instrument))
;;    (sh/sh "lilypond" "-o" "resources" "resources/temp.ly")))


(defn voice [first voiceOne melody clef tempo key-signature midi]
  (str
   "\\new Staff {
            \\clef \"" clef "\"\n
            \\tempo " tempo "\n
            \\key " (key-signature->lily key-signature)
   "\\set Staff.midiInstrument = #\"" midi
   "\"\n"

   "  \\new Voice = \"" first "\"
     { \\" voiceOne " "
   melody
   "}\n"
   "}"))

(defn end-to-1 [melody]
  ;; (println "MELODY" melody)
  (str (subs melody 0 (dec (count melody))) "1"))

(defn voices [species clef tempo key-signature midi]
  (let [[midi1 midi2] (if (vector? midi)
                        midi
                        [midi midi])
        [clef1 clef2] (if (vector? clef)
                        clef
                        [clef clef])
        cantus (get-cantus species)
        counter (get-counter species)
        position (get-position species)
        type (get-type species)
        counter->lily (case type
                        :first (partial fixed-melody->lily 1)
                        :second (fn [counter]
                                  (end-to-1 (fixed-melody->lily 2 counter)))
                        :third (fn [counter]
                                 (end-to-1 (fixed-melody->lily 4 counter)))
                        :fourth (partial fixed-melody-fourth->lily 2)
                        :fifth melody-fifth->lily
                        (throw (Exception.
                                (str "voices: not implemented species type " type))))
        counter-l (counter->lily counter)
        ;; _ (println counter-l)
        cantus-l (fixed-melody->lily 1 cantus)]
    (str

     (voice "first" "voiceOne"
            (if (= position :above)
              counter-l
              cantus-l)
            clef1 tempo key-signature midi1)

     (voice "second" "voiceTwo"
            (if (= position :above)
              cantus-l
              counter-l)
            clef2 tempo key-signature midi2)

     (figured-bass species))))

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
        position (get-position species)
        length 1]
    (str
     (voice-pattern position p length cantus counter)
     (figured-bass species))))

(defn second-voices-pattern [p species]
  (let [cantus (double-melody (get-cantus species))
        counter (get-counter species)
        position (get-position species)
        length 2]
    (str
     (voice-pattern position p length cantus counter)
     (figured-bass species))))

(defn fourth-voices-pattern [p species]
  (let [cantus (double-melody (get-cantus species))
        counter (get-counter species)
        position (get-position species)
        length 2]
    (str
     (voice-pattern position p length cantus counter)
     (figured-bass species))))

(defn voices-pattern [p species clef tempo key-signature midi]
  (case (get-type species)
    :first (first-voices-pattern p species)
    :second (second-voices-pattern p species)
    :fourth (fourth-voices-pattern p species)
    (voices species clef tempo key-signature midi)))

(defn species->lily-text
  ([species] (species->lily-text species
                                 {:clef "treble"
                                  :pattern ""
                                  :tempo "2 = 80"
                                  :key :c}))
  ([species param]
  ;;  (println "PARAM" param)
   (let [key-signature (get param :key :c)
         tempo (get param :tempo "2 = 80")
         midi (get param :midi "acoustic grand")
         param-clef (get param :clef)
         position (get-position species)
         clef (if (and param-clef (seq param-clef))
                param-clef
                (if (= position :above)
                  "treble"
                  ["treble" "treble_8"]))]
     (lilypond-score
      (let [p (get param :pattern "")]
        (if (= p "")
          (voices species clef tempo key-signature midi)
          (voices-pattern p species clef tempo key-signature midi)))))))

(defn species->lily
  ([species] (species->lily species
                            {:clef "treble"
                             :pattern ""
                             :tempo "2 = 80"
                             :file "temp"
                             :key :c}))
  ([species param]
   (println "PARAM" param)
   (let [file-name (str "resources/" (get param :file "temp") ".ly")]
     (spit file-name
           (species->lily-text species param))
     (sh/sh "lilypond" "-o" "resources" file-name))))

(defn melody->lily
  ([melody] (melody->lily melody
                          {:clef "treble"
                           :pattern ""
                           :tempo "2 = 80"
                           :midi "acoustic grand"}))
  ([cf param]
   (let [melody (get-melody cf)
         key-signature (get-key cf)
         clef (get param :clef "treble")
         tempo (get param :tempo "2 = 80")
         midi (get param :midi "acoustic grand")]
     (spit (get param :file "resources/temp.ly")
           (lilypond-score
            (let [p (get param :pattern "")]
              (if (= p "")
                (voice "first" "voiceOne" (fixed-melody->lily 1 melody)
                       clef tempo key-signature midi)
                (voice "first" "voiceOne" (fixed-melody->lily 1 melody)
                       clef tempo key-signature midi))))))
   (sh/sh "lilypond" "-o" "resources" (get param :file "resources/temp.ly"))))

(defn- markup [text]
  (str "\\markup {\n"
       text
       "\n}"))

(defn species-coll->lily [species-coll {:keys [markup-fn file folder]}]
  (let [all-str (apply str
                       (map-indexed
                        (fn [idx sp]
                          (str
                           (if markup-fn
                             (markup (markup-fn idx))
                             "")
                           (species->lily-text sp)))
                        species-coll))
        folder-name (str "resources/" folder "/")
        file-name (str "resources/" folder "/" file ".ly")]
    (println "Saving into " folder-name file-name)
    (sh/sh "mkdir" "-p" folder-name)
    (spit (str "resources/" folder "/" file ".ly") all-str)
    (sh/sh "lilypond" "-o" folder-name file-name)))

(apply str (map-indexed (fn [idx n] [idx n])
                        (range 1000)))
