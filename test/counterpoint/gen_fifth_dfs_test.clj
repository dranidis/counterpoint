(ns counterpoint.gen-fifth-dfs-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [make-cantus-firmus]]
            [counterpoint.cantus-firmi-examples :refer [fux-d]]
            [counterpoint.gen-fifth-species :refer [play-best-fifth]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(deftest gen-5th-test
  (testing "Context of the test assertions"
    (let [test-cf 
          (make-cantus-firmus :c 
                              (make-melody 
                               n/d3 
                               n/f3 
                               n/e3 
                               n/d3))]
      (play-best-fifth 1 test-cf :above)
      (is true))))