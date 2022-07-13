(ns counterpoint.melody-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [M10 P1 P8]]
            [counterpoint.melody :refer [highest-note make-melody melody-range
                                         melody-skeleton]]
            [counterpoint.notes :as n]))

(deftest melody-range-test
  (testing "P1"
    (is (= P1 (melody-range (make-melody n/a3)))))
  
  (testing "P1-many"
    (is (= P1 (melody-range (make-melody n/a3 n/a3 n/a3 n/a3)))))
  
  (testing "P8-many"
    (is (= P8 (melody-range (make-melody n/a3 n/a4 n/a3 n/a3)))))
  
  (testing "M10-many"
    (is (= M10 (melody-range (make-melody n/a4 n/c3 n/e3 n/e4 n/a4)))))
  )

(deftest melody-skeleton-test
  (testing "2 peaks"
    (is (= (make-melody n/d3 n/f3 n/d3 n/f3 n/d3) 
           (melody-skeleton (make-melody n/d3 n/e3 n/f3 
                                          n/d3 n/e3 n/f3
                                          n/e3 n/d3))))))

;; (def m (make-melody n/d3 n/e3 n/f3
;;                     n/d3 n/e3 n/f3
;;                     n/e3 n/d3))

;; (def m (make-melody n/d3 n/e3 n/f3 n/g3
;;                     n/d3 n/e3 n/f3
;;                     n/e3 n/d3))

;; (def ms (melody-skeleton m))
;; (def peaks (count (filter #(= % (highest-note ms)) ms)))
;; peaks
;; (* -20 (dec peaks))
