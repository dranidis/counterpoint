(ns counterpoint.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.intervals :refer [A2 d8 M13 m2 M2 m2- m3 M3 m3- m6
                                            M6 M7 m7 m7- M9 P1 P15 P4 P5 P8]]
            [counterpoint.core :refer [interval note->number-of-semitones]]
            [counterpoint.notes :refer [make-note a3 a4 b#3 b3 b4 bb3 bb4 c#3 c#4 c3 e3
                                        e5 f#3 f2 f3 g#3 g2 g3 g5]]))

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
    (is (= A2 (interval a3 b#3))))

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
    (is (= d8 (interval b3 bb4))))

  (testing "M9"
    (is (= M9 (interval b3 c#4))))

  (testing "M13"
    (is (= M13 (interval g3 e5))))

  (testing "P15"
    (is (= P15 (interval g3 g5))))
  ;
  )

(deftest intervals-descending
  (testing "m3"
    (is (= m3- (interval c3 a3))))

  (testing "m2"
    (is (= m2- (interval a4 g#3))))

  (testing "m7"
    (is (= m7- (interval bb4 c3)))))

(deftest note->number-of-semitones-test
  (testing "a0"
    (is (= 0 (note->number-of-semitones (make-note :a 0 :natural)))))

  (testing "a1"
    (is (= 12 (note->number-of-semitones (make-note :a 1 :natural)))))

  (testing "bb1"
    (is (= 13 (note->number-of-semitones (make-note :b 1 :flat)))))

  (testing "g#2"
    (is (= 35 (note->number-of-semitones (make-note :g 2 :sharp))))))

