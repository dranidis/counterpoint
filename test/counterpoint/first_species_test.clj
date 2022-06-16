(ns counterpoint.first-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.first-species :refer [correct-intervals ending?
                                                last-interval? make-first-species
                                                no-direct-motion-to-perfect?
                                                first-species-rules?]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]))

(deftest harmonic-intervals
  (testing "correct intervals"
    (let [counterpoint-melody
          (make-melody n/e4 n/d4 n/e4 n/bb4 n/g#3 n/a4)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= true (correct-intervals (make-first-species cantus-firmus counterpoint-melody :above))))))

  (testing "correct intervals"
    (let [counterpoint-melody
          (make-melody n/e5 n/d5 n/e5 n/bb5 n/g#4 n/a5)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= true (correct-intervals (make-first-species cantus-firmus counterpoint-melody :above))))))

  (testing "2"
    (let [counterpoint-melody
          (make-melody n/e4 n/d4 n/e4 n/e4 n/g#3 n/a4)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= false (correct-intervals (make-first-species cantus-firmus counterpoint-melody :above))))))

  (testing "4"
    (let [counterpoint-melody
          (make-melody n/e4 n/d4 n/e4 n/g4 n/g#3 n/a4)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= false (correct-intervals (make-first-species cantus-firmus counterpoint-melody :above))))))

  (testing "7"
    (let [counterpoint-melody
          (make-melody n/e4 n/d4 n/e4 n/c4 n/g#3 n/a4)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= false (correct-intervals (make-first-species cantus-firmus counterpoint-melody :above)))))))

(deftest last-interval?-test
  (testing "P1"
    (let [counterpoint-melody
          (make-melody n/a3 n/g2 n/e3 n/bb4 n/g#3 n/a3)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= true (last-interval? (make-first-species cantus-firmus counterpoint-melody :below)))))

    (testing "no P1 or P8"
      (let [counterpoint-melody
            (make-melody n/a3 n/g2 n/e3 n/bb4 n/g#3 n/g3)
            cantus-firmus
            (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
        (is (= false (last-interval? (make-first-species cantus-firmus counterpoint-melody :above))))))))

(deftest ending?-test
  (testing "leading tone"
    (let [counterpoint-melody
          (make-melody n/a3 n/g2 n/e3 n/bb4 n/g#2 n/a3)
          cantus-firmus
          (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
      (is (= true (ending? (make-first-species cantus-firmus counterpoint-melody :below)))))

    (testing "no leading tone"
      (let [counterpoint-melody
            (make-melody n/a4 n/g4 n/e4 n/bb4 n/g3 n/a4)
            cantus-firmus
            (make-melody n/a3 n/b3 n/c3 n/d3 n/b3 n/a3)]
        (is (= false (ending? (make-first-species cantus-firmus counterpoint-melody :above))))))

    (testing "phrygian"
      (let [counterpoint-melody
            (make-melody n/a4 n/g4 n/e4 n/bb4 n/d4 n/e4)
            cantus-firmus
            (make-melody n/a3 n/b3 n/c3 n/d3 n/f3 n/e3)]
        (is (= true (ending? (make-first-species cantus-firmus counterpoint-melody :above))))))

    (testing "phrygian-wrong"
      (let [counterpoint-melody
            (make-melody n/a4 n/g4 n/e4 n/bb4 n/d#4 n/e4)
            cantus-firmus
            (make-melody n/a3 n/b3 n/c3 n/d3 n/f3 n/e3)]
        (is (= false (ending? (make-first-species cantus-firmus counterpoint-melody :above))))))))


(deftest direct-test
  (testing "no direct motion to perfect"
    (let [counterpoint-melody
          (make-melody n/d3 n/d3 n/a4 n/f3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
          cantus-firmus
          (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
      (is (= true (no-direct-motion-to-perfect? (make-first-species cantus-firmus counterpoint-melody :below)))))

    (testing "direct motion to perfect"
      (let [counterpoint-melody
            (make-melody n/d3 n/d3 n/a4 n/g3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
            cantus-firmus
            (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
        (is (= false (no-direct-motion-to-perfect? (make-first-species cantus-firmus counterpoint-melody :below))))))))

(deftest species-test
  (testing "correct"
    (let [counterpoint-melody
          (make-melody n/d3 n/d3 n/a4 n/f3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
          cantus-firmus
          (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
      (is (= true (first-species-rules? (make-first-species cantus-firmus counterpoint-melody :below)))))

    (testing "wrong"
      (let [counterpoint-melody
            (make-melody n/d3 n/d3 n/a4 n/g3 n/e3 n/d3 n/c3 n/e3 n/d3 n/c#3 n/d3)
            cantus-firmus
            (make-melody n/d4 n/f4 n/e4 n/d4 n/g4 n/f4 n/a5 n/g4 n/f4 n/e4 n/d4)]
        (is (= false (first-species-rules? (make-first-species cantus-firmus counterpoint-melody :below))))))))



