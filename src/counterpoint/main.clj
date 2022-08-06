(ns counterpoint.main
  (:require [clojure.java.shell :as sh]
            [clojure.tools.cli :refer [parse-opts]]
            [counterpoint.cantus-firmi-examples :refer [cf-catalog]]
            [counterpoint.generate :refer [generate]]
            [counterpoint.lilypond :refer [melody->lily]])
  (:gen-class))

(def cli-options
  [;;    
   ["-c" "--cantus CANTUS"  "Cantus firmus"
    :default "fux-d"]
   ["-l" "--list"]
   ["-P" "--play"]
   ["-p" "--position POSITION" "Position of counterpoint"
    :default "above"]
   ["-g" "--generate"]
   ["-s" "--species TYPE" "Species type"
    :default "first"]
   ["-N" "--takeN NUMBER" "How many samples to generate for the dfs search"
    :default 100
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %)]]
   ["-h" "--help"]
;;    
   ])

(defn -main [& args]
  (let [parsed-args (parse-opts args cli-options)
        ;; _ (println parsed-args)
        c-option (get-in parsed-args [:options :cantus])
        cantus (get cf-catalog (keyword c-option))
        species-type (keyword (get-in parsed-args [:options :species]))
        position (keyword (get-in parsed-args [:options :position]))
        take-n (get-in parsed-args [:options :takeN])
        gen-species (when (get-in parsed-args [:options :generate])
                      (println "Generating" species-type)
                      (generate species-type take-n cantus position))]
    (when (get-in parsed-args [:options :list])
      (println "Available cantus-firmi")
      (doseq [c (sort (map name (keys cf-catalog)))]
        (println c)))
    
    (when (get-in parsed-args [:options :play])
      (when (nil? gen-species)
        (melody->lily cantus))
      (sh/sh "timidity" "resources/temp.midi")
      )
    ))
  