(ns counterpoint.gen-second-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.gen-second-dfs :refer [generate-reverse-counterpoint-2nd-dfs]]
            [counterpoint.second-species :refer [make-second-species
                                                 second-species-rules?]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest generate-second-test
  (testing "above"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-2nd-dfs :above (get-key fux-d) fux-d-cf)
          cp (dfs-solution->cp (first cps))
          species (make-second-species fux-d-cf cp :above)]
      ;; (println species)
      (is (second-species-rules? species))))

  (testing "below"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-2nd-dfs :below (get-key fux-d) fux-d-cf)
          cp (dfs-solution->cp (second cps))
          species (make-second-species fux-d-cf cp :below)]
      ;; (pprint/pprint species)
      ;; (species->lily species {:clef "treble_8"})
      (is (second-species-rules? species)))))