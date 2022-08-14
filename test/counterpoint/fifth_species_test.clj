(ns counterpoint.fifth-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [get-melody]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.fifth-species :refer [counter-cantus
                                                make-fifth-species]]
            [counterpoint.lilypond :refer [species->lily]]
            [counterpoint.notes :as n]
            [counterpoint.rest :as r]))

(deftest counter-cantus-test
  (testing "Cantus with durations matching the counter (one bar)"
    (let [notes [[{:d 2  :n [r/r n/a4]}]]
          expected [[{:d 2 :n [n/d3 n/d3]}]]
          actual (counter-cantus notes
                                 (get-melody fux-d))]
      (is (= expected actual))))

  (testing "Cantus with durations matching the counter (a list per bar)"
    (let [notes [[{:d 2  :n [r/r n/a4]}]
                 [{:d 4 :n [n/a4 n/d3 n/e3 n/f3]}]]
          expected [[{:d 2 :n [n/d3 n/d3]}]
                    [{:d 4 :n [n/f3 n/f3 n/f3 n/f3]}]]
          actual (counter-cantus notes
                                 (get-melody fux-d))]
      (is (= expected actual))))

  (testing "Cantus with durations matching the counter (a bar with two lists)"
    (let [notes [[{:d 2  :n [r/r n/a4]}]
                 [{:d 4 :n [n/a4 n/d3 n/e3 n/f3]}]
                 [{:d 4 :n  [n/g3 n/f3 n/e3 n/g3]}]
                 [{:d 4 :n  [n/f3 n/d3]} {:d 2 :n [n/d4]}]]
          expected [[{:d 2 :n [n/d3 n/d3]}]
                    [{:d 4 :n [n/f3 n/f3 n/f3 n/f3]}]
                    [{:d 4 :n [n/e3 n/e3 n/e3 n/e3]}]
                    [{:d 4 :n  [n/d3 n/d3]} {:d 2 :n [n/d3]}]]
          actual (counter-cantus notes
                                 (get-melody fux-d))]
      (is (= expected actual))))

  (testing "Cantus with durations matching the counter"
    (let [notes [[{:d 2  :n [r/r n/a4]}]
                 [{:d 4 :n [n/a4 n/d3 n/e3 n/f3]}]
                 [{:d 4 :n [n/g3 n/f3 n/e3 n/g3]}]
                 [{:d 4 :n [n/f3 n/d3]} {:d 2 :n [n/d4]}]
                 [{:d 4 :n [n/d4 n/c4 n/bb4 n/g3]}]
                 [{:d 4 :n [n/a4 n/b4]} {:d 2 :n [n/c4]}]
                 [{:d 2 :n  [n/c4 n/f4]}]
                 [{:d 4 :n [n/f4]} {:d 8 :n [n/e4 n/d4]} {:d 2 :n [n/e4]}]
                 [{:d 4 :n [n/e4 n/a4]} {:d 2 :n [n/d4]}]
                 [{:d 2 :n [n/d4 n/c#4]}]
                 [{:d 1 :n [n/d4]}]]
          expected [[{:d 2 :n [n/d3 n/d3]}]
                    [{:d 4 :n [n/f3 n/f3 n/f3 n/f3]}]
                    [{:d 4 :n [n/e3 n/e3 n/e3 n/e3]}]
                    [{:d 4 :n [n/d3 n/d3]} {:d 2 :n [n/d3]}]
                    [{:d 4 :n [n/g3 n/g3 n/g3 n/g3]}]
                    [{:d 4 :n [n/f3 n/f3]} {:d 2 :n [n/f3]}]
                    [{:d 2 :n [n/a4 n/a4]}]
                    [{:d 4 :n [n/g3]} {:d 8 :n [n/g3 n/g3]} {:d 2 :n [n/g3]}]
                    [{:d 4 :n [n/f3 n/f3]} {:d 2 :n [n/f3]}]
                    [{:d 2 :n [n/e3 n/e3]}]
                    [{:d 1 :n [n/d3]}]]
          actual (counter-cantus notes (get-melody fux-d))]
      (is (= expected actual)))))
