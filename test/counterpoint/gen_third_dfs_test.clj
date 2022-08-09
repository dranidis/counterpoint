(ns counterpoint.gen-third-dfs-test 
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d fux-e]]
            [counterpoint.gen-third-dfs :refer [generate-reverse-counterpoint-3rd-dfs]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.third-species :refer [make-third-species
                                                third-species-rules?]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest generate-third-test
  (testing "above"
    (let [cf (make-melody n/f3 n/e3 n/d3)
          cps (generate-reverse-counterpoint-3rd-dfs :above :c cf)
          cp (dfs-solution->cp (nth cps 0))
          species (make-third-species cf cp :above)]
    ;;   (println species)
    ;;   (species->lily species)
      (is (third-species-rules? species))))

  )

(deftest generate-third-fux-test
  (testing "above"
    (let [cf (get-melody fux-d)
          cps (generate-reverse-counterpoint-3rd-dfs 
               :above (get-key fux-d) cf)
          cp (dfs-solution->cp (nth cps 0))
          species (make-third-species cf cp :above)]
      ;; (println species)
      ;; (species->lily species)
      (is (third-species-rules? species)))))

(deftest generate-third-fux-e-test
  (testing "above"
    (let [cantus fux-e
          cf (get-melody cantus)
          cps (generate-reverse-counterpoint-3rd-dfs
               :above (get-key cantus) cf)
          cp (dfs-solution->cp (nth cps 0))
          species (make-third-species cf cp :above)]
      ;; (println species)
      ;; (species->lily species)
      (is (third-species-rules? species)))))