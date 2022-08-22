(ns counterpoint.gen-fifth-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [make-cantus-firmus]]
            [counterpoint.fifth-species :refer [evaluate-fifth-species
                                                make-fifth-species]]
            [counterpoint.gen-fifth-species :refer [candidates
                                                    elaborate-4th-species generate-fifth
                                                    next-node-fifth]]
            [counterpoint.generate :refer [generate-reverse-counterpoint-dfs]]
            [counterpoint.lilypond :refer [species-coll->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest gen-5th-test
  (testing "Gen a florid melody"
    (let [test-cf
          (make-cantus-firmus :c
                              (make-melody
                               n/d3
                               n/f3
                               n/e3
                               n/d3))
          sp (generate-fifth 1 test-cf :above 
                             {:file "test_gen_5"})]
      (is (not (nil? sp))))))

(deftest generate-fifth-all-eval
  (testing "score with all"
    (let [position :above
          key-sig :g
          test-cf
          (make-melody n/g3 n/c4 n/b4 n/a4 n/g3)
          cps (generate-reverse-counterpoint-dfs position 
                                                 key-sig
                                                 test-cf
                                                 candidates
                                                 next-node-fifth)
          sp-coll (mapv #(elaborate-4th-species (make-fifth-species test-cf
                                              (dfs-solution->cp %)
                                              position) key-sig)
                        cps)
          scores (map #(evaluate-fifth-species %) sp-coll)
          markup-fn (fn [idx]
                      ;; (println idx (nth scores idx))
                      (str "Ex. " idx " score: " (nth scores idx)))]
      (println "CPS" (count cps))
      (species-coll->lily sp-coll
                          {:markup-fn markup-fn
                           :file "all"
                           :folder "fifth"}))))