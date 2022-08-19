(ns counterpoint.gen-first-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.first-species :refer [evaluate-first-species
                                                first-species-rules?
                                                make-first-species]]
            [counterpoint.gen-first-dfs :refer [candidates]]
            [counterpoint.generate :refer [generate-reverse-counterpoint-dfs]]
            [counterpoint.generate-first-species :refer [next-reverse-candidates-1st]]
            [counterpoint.lilypond :refer [species-coll->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest generate-first-test
  (testing "above"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :above :c fux-d-cf candidates)
          cp (dfs-solution->cp (first cps))
          species (make-first-species fux-d-cf cp :above)]
      (is (first-species-rules? species))))

  (testing "below"
    (let [fux-d-cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-dfs :below :c fux-d-cf candidates)
          cp (dfs-solution->cp (first cps))
          species (make-first-species fux-d-cf cp :below)]
      (is (first-species-rules? species)))))

(deftest next-reverse-candidates-1st-test
  (testing "below direct perfect 5"
    (let [state {:position :below
                 :key :c
                 :melody [n/e3 n/f3]
                 :m36s {:thirds 0 :sixths 0 :tens 0 :thirteens 0}
                 :previous-cantus n/c4
                 :previous-melody n/f3
                 :cantus-note n/d4}
          cand (next-reverse-candidates-1st state)
          _ (println cand)]
      (is (nil? ((set cand) n/a5))))))

(deftest generate-first-all-eval
  (testing "evaluation of 1st species"
    (let [test-cf
          (make-melody n/d3 n/a4 n/g3 n/f3 n/e3 n/d3)
          cps (generate-reverse-counterpoint-dfs :above :c test-cf
                                                 candidates)
          sp-coll (mapv #(make-first-species test-cf
                                              (dfs-solution->cp %)
                                              :above)
                        cps)
          scores (map #(evaluate-first-species %) sp-coll)
          markup-fn (fn [idx] (str "Ex. " idx " score: " (nth scores idx)))]
      (println "CPS" (count cps))
      (species-coll->lily sp-coll
                          {:markup-fn markup-fn
                           :file "all"
                           :folder "first"}))))