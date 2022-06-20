(ns counterpoint.notes-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.notes :refer [num2->note]]))

(deftest num2->note-test
  (testing "0"
    (is (= [:a 0]
           (num2->note 0))))

  (testing "1"
    (is (= [:b 0]
           (num2->note 1))))

  (testing "2"
    (is (= [:c 0]
           (num2->note 2))))

  (testing "3"
    (is (= [:d 0]
           (num2->note 3))))

  (testing "4"
    (is (= [:e 0]
           (num2->note 4))))

  (testing "4"
    (is (= [[:a 0] [:b 0] [:c 0] [:d 0] [:e 0] [:f 0] [:g 0] [:a 1] [:b 1] [:c 1] [:d 1] [:e 1] [:f 1] [:g 1] [:a 2]]
           (map num2->note (range 15)))))
  -

  (testing "-1"
    (is (= [:g -1]
           (num2->note -1))))

  (testing "-7"
    (is (= [:a -1]
           (num2->note -7))))

  (testing "4"
    (is (= [[:g -3]
            [:a -2]
            [:b -2]
            [:c -2]
            [:d -2]
            [:e -2]
            [:f -2]
            [:g -2]
            [:a -1]
            [:b -1]
            [:c -1]
            [:d -1]
            [:e -1]
            [:f -1]
            [:g -1]
            [:a 0]]
           (map num2->note (range -15 1))))))

