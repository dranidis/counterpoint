(ns counterpoint.generate

  (:require [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(defn generate-template
  [generate-reverse-counterpoint-dfs-fn
   evaluate-species-fn
   make-species-fn
   species-rules-fn?]
  (fn [n cantus position options]
    ;; (println options)
    (let [key (get-key cantus)
          cf (get-melody cantus)
          cps (take n (generate-reverse-counterpoint-dfs-fn position key cf))
          _ (println "ALL" (count cps))
          ;; _ (println "CPS" cps)
          species (apply max-key #(let [e (evaluate-species-fn  %)]
                                ;; (println e)
                                    e)
                         (map #(make-species-fn cf (dfs-solution->cp %) position) cps))
          _ (println "RULES " (species-rules-fn? species))
          _ (println "EVAL  " (evaluate-species-fn species))
          ;; _ (println "SPECIES" species)
          ]
      (species->lily species
                     {:clef
                      (if (= position :above)
                        "treble"
                        "treble_8")
                      :pattern (get options :pattern "")
                      :tempo (str "4 = " (get options :tempo))
                      :key key
                      :midi (get options :midi)})
      species)))

