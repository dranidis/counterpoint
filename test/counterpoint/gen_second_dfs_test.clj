(ns counterpoint.gen-second-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.gen-second-dfs :refer [candidates]]
            [counterpoint.generate :refer [generate-reverse-counterpoint-dfs]]
            [counterpoint.lilypond :refer [species-coll->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [evaluate-second-species
                                                 make-second-species
                                                 second-species-rules?]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest generate-second-test
  (testing "above"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :above (get-key fux-d) fux-d-cf candidates)
          cp (dfs-solution->cp (first cps))
          species (make-second-species fux-d-cf cp :above)]
      ;; (println species)
      (is (second-species-rules? species))))

  (testing "below"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :below (get-key fux-d) fux-d-cf candidates)
          cp (dfs-solution->cp (second cps))
          species (make-second-species fux-d-cf cp :below)]
      ;; (pprint/pprint species)
      ;; (species->lily species {:clef "treble_8"})
      (is (second-species-rules? species)))))

(deftest generate-second-all-eval
  (testing "evaluation of 1st species"
    (let [test-cf
          (make-melody n/d3 n/f3 n/e3 n/g3 n/f3 n/e3 n/d3)
          cps (generate-reverse-counterpoint-dfs :above :c test-cf
                                                 candidates)
          sp-coll (mapv #(make-second-species test-cf
                                             (dfs-solution->cp %)
                                             :above)
                        cps)
          scores (map #(evaluate-second-species %) sp-coll)
          markup-fn (fn [idx] (str "Ex. " idx " score: " (nth scores idx)))]
      (println "CPS" (count cps))
      (species-coll->lily sp-coll
                          {:markup-fn markup-fn
                           :file "all"
                           :folder "second"}))))