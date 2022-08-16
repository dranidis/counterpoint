(ns counterpoint.gen-fifth-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [make-cantus-firmus]]
            [counterpoint.gen-fifth-species :refer [generate-fifth]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(deftest gen-5th-test
  (testing "Gen a florid melody"
    (let [test-cf
          (make-cantus-firmus :c
                              (make-melody
                               n/d3
                               n/f3
                               n/e3
                               n/d3))
          sp (generate-fifth 1 test-cf :above {})]
      (is (not (nil? sp))))))