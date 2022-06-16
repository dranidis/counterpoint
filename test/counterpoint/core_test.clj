(ns counterpoint.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.core :refer [a3 a4 b#3 b3 b4 bb3 bb4 c#3 c3 e3 f2 g2 c#4 g3 e4 e5 g5
                                       interval]]))

(deftest intervals-ascending
  (testing "unison"
    (is (= [1 :perfect] (interval a3 a3))))

  (testing "fifth"
    (is (= [5 :perfect] (interval a3 e3))))

  (testing "octave"
    (is (= [8 :perfect] (interval a3 a4))))

  (testing "m2"
    (is (= [2 :minor] (interval a3 bb3))))

  (testing "M2"
    (is (= [2 :major] (interval a3 b3))))

  (testing "a2"
    (is (= [2 :aug] (interval a3 b#3))))

  (testing "m3"
    (is (= [3 :minor] (interval a3 c3))))

  (testing "M3"
    (is (= [3 :major] (interval a3 c#3))))
  
  (testing "P4"
    (is (= [4 :perfect] (interval g2 c3))))
  
  (testing "P5"
    (is (= [5 :perfect] (interval f2 c3))))
  
  (testing "P8"
    (is (= [8 :perfect] (interval bb3 bb4))))
  
  (testing "M7"
    (is (= [7 :major] (interval c3 b4))))  
  
  (testing "d8"
    (is (= [8 :dim] (interval b3 bb4))))
  
  (testing "M9"
    (is (= [9 :major] (interval b3 c#4))))
  
    (testing "M13"
      (is (= [13 :major] (interval g3 e5))))
  
      (testing "P15"
        (is (= [15 :perfect] (interval g3 g5))))
  ;
  )

(deftest intervals-descending
  (testing "m3"
    (is (= [-3 :minor] (interval c3 a3))))
  
  (testing "m7"
    (is (= [-7 :minor] (interval bb4 c3)))))
