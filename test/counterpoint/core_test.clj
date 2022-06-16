(ns counterpoint.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.core :refer [a3 a4 b#3 b3 b4 bb3 bb4 c#3 c#4 c3 e3
                                       e5 f#3 f2 f3 g#3 g2 g3 g5 interval]]
            [counterpoint.intervals :refer [m2 M2 m3 M3 m6 M6 M7 m7 P1 P4 P5
                                            P8]]))

(deftest intervals-ascending
  (testing "unison"
    (is (= P1 (interval a3 a3))))

  (testing "fifth"
    (is (= P5 (interval a3 e3))))

  (testing "octave"
    (is (= P8 (interval a3 a4))))

  (testing "m2"
    (is (= m2 (interval a3 bb3))))

  (testing "M2"
    (is (= M2 (interval a3 b3))))

  (testing "a2"
    (is (= [2 :aug] (interval a3 b#3))))

  (testing "m3"
    (is (= m3 (interval a3 c3))))

  (testing "M3"
    (is (= M3 (interval a3 c#3))))
  
  (testing "P4"
    (is (= P4 (interval g2 c3))))
  
  (testing "P5"
    (is (= P5 (interval f2 c3))))
  
  (testing "P8"
    (is (= P8 (interval bb3 bb4))))
  
  (testing "m6"
    (is (= m6 (interval a3 f3))))

  (testing "M6"
    (is (= M6 (interval a3 f#3))))

  (testing "m7"
    (is (= m7 (interval c3 bb4))))

  (testing "M7"
    (is (= M7 (interval c3 b4))))
  
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
  
  (testing "m2"
    (is (= [-2 :minor] (interval a4 g#3))))
  
  (testing "m7"
    (is (= [-7 :minor] (interval bb4 c3)))))
