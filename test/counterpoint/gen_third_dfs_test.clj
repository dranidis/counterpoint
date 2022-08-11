(ns counterpoint.gen-third-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-key get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d fux-e]]
            [counterpoint.gen-third-dfs :refer [candidates
                                                generate-reverse-counterpoint-3rd-dfs]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.third-species :refer [make-third-species
                                                third-species-rules?]]
            [counterpoint.third-species-patterns :refer [desc-pt-skip-3-to
                                                         pattern-fits?]]
            [counterpoint.utils :refer [dfs-solution->cp]]))

(deftest generate-third-test
  (testing "above"
    (let [cf (make-melody n/f3 n/e3 n/d3)
          cps (generate-reverse-counterpoint-3rd-dfs :above :c cf)
          cp (dfs-solution->cp (nth cps 0))
          species (make-third-species cf cp :above)]
    ;;   (println species)
    ;;   (species->lily species)
      (is (third-species-rules? species)))))

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

(deftest candidates-test
  (testing "candidates with no dim5"
    (let [position :below
          cantus-key :c
          melody (make-melody n/b3 n/a3 n/g2 n/b3)
          previous-melody n/b3
          previous-cantus n/g3
          cantus-note n/c3
          cantus-notes []
          m36s {:remaining-cantus-size (inc (count cantus-notes))}
          state {:position position
                 :key cantus-key
                 :melody melody
                 :m36s m36s
                 :previous-melody previous-melody
                 :previous-cantus previous-cantus
                 :cantus-note cantus-note
                 :cantus-notes cantus-notes}
          cand (candidates state)
          pat (desc-pt-skip-3-to {:key cantus-key :previous-melody previous-melody})]
      (is (contains? (set cand) pat)))))
