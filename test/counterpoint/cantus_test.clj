(ns counterpoint.cantus-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.cantus :refer [cantus-rules? final-note-approach?
                                         first-last-same make-cantus-firmus
                                         maximum-range-M10?]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as notes]))

(deftest maximum-range-M10
  (testing "P1"
    (is (= true (maximum-range-M10? (make-melody notes/a3)))))
  
  (testing "M10"
    (is (= true (maximum-range-M10? (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/a4)))))

  (testing "m11"
    (is (= false (maximum-range-M10? (make-melody notes/a4 notes/c3 notes/e3 notes/f4 notes/a4)))))

  
  
  )

(deftest maximum-range-M10
  (testing "P1"
    (is (= true (first-last-same (make-melody notes/a3)))))

  (testing "M10"
    (is (= true (first-last-same (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/a4)))))

  (testing "m11"
    (is (= true (first-last-same (make-melody notes/a4 notes/c3 notes/e3 notes/f4 notes/a4)))))
  
  (testing "m11"
    (is (= false (first-last-same (make-melody notes/a4 notes/c3 notes/e3 notes/f4))))))

(deftest maximum-final-note-approach
  (testing "P1"
    (is (= false (final-note-approach? (make-melody notes/a3)))))

  (testing "M10"
    (is (= false (final-note-approach? (make-melody notes/a4 notes/c3 notes/e3 notes/e4 notes/a4)))))

  (testing "second"
    (is (= true (final-note-approach? (make-melody notes/a4 notes/c3 notes/e3 notes/f4 notes/b4 notes/a4)))))

  (testing "second min"
    (is (= true (final-note-approach? (make-melody notes/a4 notes/c3 notes/e3 notes/f4 notes/c4 notes/b4)))))

  (testing "leading tone"
    (is (= true (final-note-approach? (make-melody notes/a4 notes/c3 notes/e3 notes/f3)))))

  (testing "leading tone low"
    (is (= false (final-note-approach? (make-melody notes/a4 notes/c3 notes/e3 notes/f#3))))))


(deftest cantus-test
  
  (testing "correct"
    (is (= true (cantus-rules? (make-cantus-firmus notes/a4 (make-melody notes/a4 notes/c4 notes/e4 notes/b4 notes/a4))))))
  
  (testing "wrong mode"
    (is (= false (cantus-rules? (make-cantus-firmus notes/c4 (make-melody notes/a4 notes/c4 notes/e4 notes/b4 notes/a4))))))
  
  (testing "note repetition"
    (is (= false (cantus-rules? (make-cantus-firmus notes/a4 (make-melody notes/a4 notes/c4 notes/c4 notes/e4 notes/b4 notes/a4)))))))


