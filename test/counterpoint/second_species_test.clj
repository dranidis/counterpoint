(ns counterpoint.second-species-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.melody :refer [make-melody]]
            [counterpoint.notes :as n]
            [counterpoint.second-species :refer [downbeats-second passing-tone]]))

(deftest downbeats-test
  (testing "one note melody"
    (let [counterpoint-melody
          (make-melody n/c4)]
      (is (= (make-melody n/c4)
             (downbeats-second counterpoint-melody)))))

  (testing "melody"
    (let [counterpoint-melody
          (make-melody n/g4 n/b4
                       n/a4 n/b4
                       n/c4)]
      (is (= (make-melody n/g4
                          n/a4
                          n/c4)
             (downbeats-second counterpoint-melody))))))

(deftest passing-tone-test
  (testing "g f e"
    (is (passing-tone [n/g4 n/f4 n/e4])))
  (testing "f g a"
    (is (passing-tone [n/f4 n/g4 n/a5])))
  (testing "f g a'"
    (is (not (passing-tone [n/f4 n/g4 n/a4])))))

