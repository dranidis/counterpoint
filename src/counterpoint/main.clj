(ns counterpoint.main
  (:require [clojure.java.shell :as sh]
            [clojure.tools.cli :refer [parse-opts]]
            [counterpoint.cantus :refer [get-key get-melody make-cantus-firmus]]
            [counterpoint.cantus-firmi-examples :refer [cf-catalog]]
            [counterpoint.first-species-type :refer [get-counter]]
            [counterpoint.gen-first-dfs :refer [generate-first]]
            [counterpoint.gen-fourth-dfs :refer [generate-fourth]]
            [counterpoint.gen-second-dfs :refer [generate-second]]
            [counterpoint.lilypond :refer [melody->lily]]
            [counterpoint.melody :refer [transpose]])
  (:gen-class))

(def cli-options
  [;;    
   ["-c" "--cantus CANTUS"  "Cantus firmus"
    :default "fux-d"]
   ["-l" "--list"]
   ["-P" "--play"]
   ["-p" "--position POSITION" "Position of counterpoint"
    :default "above"]
   ["-T" "--pattern PATTERN" "Generated pattern"
    :default ""]
   ["-m" "--midi INSTRUMENT" "MIDI Instrument"
    :default "acoustic grand piano"]
   ["-g" "--generate" "Generate species"]
   ["-s" "--species TYPE" "Species type"
    :default "first"]
   ["-S" "--solo" "Solo the counterpoint"]
   ["-N" "--takeN NUMBER" "How many samples to generate for the dfs search"
    :default 100
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %)]]
   ["-t" "--transpose OCTAVES" "Transpose the melody by octaves"
    :default 0
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= -2 % 2)]]
   ["-h" "--help"]
;;    
   ])

(defn generate
  [species-type n cantus position options]
  (case species-type
    :first (generate-first n cantus position options)
    :second (generate-second n cantus position options)
    :fourth (generate-fourth n cantus position options)))

(defn -main [& args]
  (let [parsed-args (parse-opts args cli-options)
        _ (when (get-in parsed-args [:options :help])
            (println (get-in parsed-args [:summary])))
        c-option (get-in parsed-args [:options :cantus])
        transpose-by (get-in parsed-args [:options :transpose])
        cantus (get cf-catalog (keyword c-option))
        key-sig (get-key cantus)
        transposed-cantus (make-cantus-firmus (get-key cantus)
                                              (transpose (get-melody cantus)
                                                         transpose-by))
        species-type (keyword (get-in parsed-args [:options :species]))
        position (keyword (get-in parsed-args [:options :position]))
        take-n (get-in parsed-args [:options :takeN])
        gen-species (when (get-in parsed-args [:options :generate])
                      (println "Generating" species-type)
                      (generate species-type
                                take-n
                                transposed-cantus
                                position
                                {:pattern (get-in parsed-args [:options :pattern])
                                 :midi (get-in parsed-args [:options :midi])}))]



    (when (get-in parsed-args [:options :list])
      (println "Available cantus-firmi")
      (doseq [c (sort (map name (keys cf-catalog)))]
        (print c  " "))
      (println))

    (when (get-in parsed-args [:options :play])
      (when (nil? gen-species)
        (melody->lily transposed-cantus))
      (when (get-in parsed-args [:options :solo])
        (let [cp (make-cantus-firmus
                  key-sig
                  (get-counter gen-species))]
          (melody->lily cp)))
      (sh/sh "timidity" "resources/temp.midi"))))
