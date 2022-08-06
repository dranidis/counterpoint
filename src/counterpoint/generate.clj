(ns counterpoint.generate 
  
  (:require [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.gen-first-dfs :refer [generate-first]]
            [counterpoint.gen-fourth-dfs :refer [generate-fourth]]
            [counterpoint.gen-second-dfs :refer [generate-second]]))

(defn generate
  [species-type n cantus-f position]
  (let [cantus (get-melody cantus-f)
        key (get-key cantus-f)]
    (case species-type
    :first (generate-first n cantus key position)
    :second (generate-second n cantus key position)
    :fourth (generate-fourth n cantus key position))))