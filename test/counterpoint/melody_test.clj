(ns counterpoint.melody-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [M10 P1 P8]]
            [counterpoint.melody :refer [make-melody melody-range]]
            [counterpoint.notes :as notes]))

(deftest melody-range-test
  (testing "P1"
    (is (= P1 (melody-range (make-melody notes/a3)))))
  
  (testing "P1-many"
    (is (= P1 (melody-range (make-melody notes/a3 notes/a3 notes/a3 notes/a3)))))
  
  (testing "P8-many"
    (is (= P8 (melody-range (make-melody notes/a3 notes/a4 notes/a3 notes/a3)))))
  
  (testing "M10-many"
    (is (= M10 (melody-range (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/a4)))))
  )

