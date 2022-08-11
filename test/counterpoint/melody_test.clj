(ns counterpoint.melody-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [M10 P1 P8]]
            [counterpoint.melody :refer [get-up-downs make-melody melody-range
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
    (is (= M10 (melody-range (make-melody n/a4 n/c3 n/e3 n/e4 n/a4))))))

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


(deftest asc-desc-test
      (testing "1 up down"
        (let [mel (make-melody n/c3 n/d3 n/e3 n/d3 n/c3)
              up-downs (get-up-downs mel)]
          (is (= 1 up-downs))))
  
  (testing "2 up down"
    (let [mel (make-melody n/c3 n/d3 n/e3 n/d3 n/c3 n/b3 n/c3 n/d3 n/c3 n/b3)
          up-downs (get-up-downs mel)]
      (is (= 2 up-downs))))
  
  (testing "no up down"
    (let [mel (make-melody [:c 4 :natural] [:b 4 :natural] [:a 4 :natural] [:c 4 :natural] [:b 4 :natural] [:c 4 :natural] [:d 4 :natural] [:b 4 :natural] [:e 4 :natural] [:b 4 :natural] [:e 4 :natural] [:d 4 :natural] [:c 4 :natural] [:b 4 :natural] [:a 4 :natural] [:c 4 :natural] [:b 4 :natural] [:c 4 :natural] [:d 4 :natural] [:b 4 :natural] [:e 4 :natural] [:e 3 :natural] [:f 3 :natural] [:g 3 :natural] [:a 4 :natural] [:b 4 :natural] [:c 4 :natural] [:a 4 :natural] [:d 4 :natural] [:d 3 :natural] [:e 3 :natural] [:f 3 :natural] [:g 3 :natural] [:a 4 :natural] [:b 4 :natural] [:g 3 :natural] [:c 4 :natural] [:c 3 :natural] [:d 3 :natural] [:e 3 :natural] [:f 3 :natural] [:g 3 :natural] [:a 4 :natural] [:b 4 :natural] [:c 4 :natural])
          up-downs (get-up-downs mel)]
      (is (= 0 up-downs))))) 

