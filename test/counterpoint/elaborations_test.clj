(ns counterpoint.elaborations-test
  (:require [clojure.test :refer [deftest is testing]]
            [counterpoint.elaborations :refer [elaborate-suspension-with-next-working-bar
                                               fourth-diminution-to-previous]]
            [counterpoint.notes :as n]))

(deftest fourth-diminution-high-to-previous-test
  (testing "last note 2 alone in the list"
    (let [key-sig :c
          bar [{:d 2, :n [[:a 4 :natural]]} {:d 4, :n [[:a 4 :natural] [:b 4 :natural]]}]
          previous-melody n/d4
          actual (fourth-diminution-to-previous key-sig bar previous-melody)
          expected [{:d 8, :n [n/c4 n/b4]}
                    {:d 4, :n [[:a 4 :natural]]}
                    {:d 4, :n [[:a 4 :natural] [:b 4 :natural]]}]]
      (is (= expected actual))))

  (testing "last note 2 in a list with another note"
    (let [key-sig :c
          bar [{:d 2, :n [[:c 4 :natural] [:d 4 :natural]]}]
          previous-melody n/f4
          actual (fourth-diminution-to-previous key-sig bar previous-melody)
          expected [{:d 8, :n [n/e4 n/d4]}
                    {:d 4, :n [[:c 4 :natural]]}
                    {:d 2, :n [[:d 4 :natural]]}]]
      (is (= expected actual)))))

(deftest elaborate-suspension-with-next-working-bar-test
  (testing "testing with a bar with 2 notes dur 2"
    (let [elaborated-bar [{:d 2, :n [[:b 5 :natural] [:e 4 :natural]]}]
          previous-working-bar [{:d 2, :n [[:f 4 :natural] [:g 4 :natural]]}]
          actual (elaborate-suspension-with-next-working-bar
                  elaborated-bar previous-working-bar)
          expected [{:d 2 :n [n/b5]}
                    {:d 4 :n [n/e4 n/f4]}]]
      (is (= expected actual)))))
