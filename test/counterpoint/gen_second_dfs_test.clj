(ns counterpoint.gen-second-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.gen-first-dfs :refer [dfs-solution->cp]]
            [counterpoint.gen-second-dfs :refer [generate-reverse-counterpoint-2nd-dfs]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.second-species :refer [make-second-species
                                                 second-species-rules?]]))

(deftest generate-second-test
  (testing "above"
    (let [cps (generate-reverse-counterpoint-2nd-dfs :above :c fux-d)
          cp (dfs-solution->cp (first cps))
          species (make-second-species fux-d cp :above)]
      ;; (println species)
      (is (second-species-rules? species))))
  
  (testing "below"
    (let [cps (generate-reverse-counterpoint-2nd-dfs :below :c fux-d)
          cp (dfs-solution->cp (second cps))
          species (make-second-species fux-d cp :below)]
      ;; (pprint/pprint species)
      ;; (species->lily species {:clef "treble_8"})
      (is (second-species-rules? species)))))